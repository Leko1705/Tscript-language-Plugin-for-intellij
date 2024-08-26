package com.tscript.ide.analysis.hints;

import com.github.weisj.jsvg.C;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.tscript.ide.analysis.DefinitionCheckAnnotator;
import com.tscript.ide.analysis.LazyAnnotationBuilder;
import com.tscript.ide.psi.*;
import com.tscript.ide.reference.TscriptDirectNavigationProvider;
import com.tscript.lang.tscriptc.analysis.DefinitionChecker;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ConstantConditionDetector implements Annotator {

    private static final String EXPRESSION_ALWAYS_FALSE = "expression is always false";
    private static final String EXPRESSION_ALWAYS_TRUE = "expression is always true";

    private static final String CONDITION_ALWAYS_FALSE = "condition is always false";
    private static final String CONDITION_ALWAYS_TRUE = "condition is always true";

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiFile){
            element.accept(new Handler(holder));
        }
        else if (element instanceof TestFunctionDef f && f.getBlock() != null){
            f.getBlock().accept(new Handler(holder));
        }
        else if (element instanceof TestLambdaExpr l && l.getBlock() != null){
            l.getBlock().accept(new Handler(holder));
        }
    }



    private interface Value<C> {

        String type();

        C content();

        default boolean compareType(Value<?> value){
            return isOfType(value.type());
        }

        default boolean compareContent(Value<?> value){
            if (content() == null) return value.content() == null;
            return content().equals(value.content());
        }

        default boolean isOfType(String type){
            if (type() == null) return type == null;
            return type().equals(type);
        }
    }

    private static class Unknown implements Value<Void> {

        public static final Unknown INSTANCE = new Unknown();

        @Override
        public String type() {
            return null;
        }

        @Override
        public Void content() {
            return null;
        }

        @Override
        public boolean compareType(Value<?> value) {
            return true;
        }

        @Override
        public boolean compareContent(Value<?> value) {
            return true;
        }
    }

    private record ComplexValue(String type) implements Value<String> {

        @Override
        public String  content() {
            return UUID.randomUUID().toString();
        }
    }

    private record HoldingValue<C>(String type, C c) implements Value<C> {

        @Override
        public C content() {
            return c;
        }
    }

    private record MirrorTypeValue(Value<?> value) implements Value<String> {

        @Override
        public String type() {
            return "Type";
        }

        @Override
        public String content() {
            return value.type();
        }

    }


    private static class Handler extends TestVisitor {

        private record Scope(Map<String, Value<?>> values, Set<String> changes){}

        private final AnnotationHolder holder;

        private final Deque<Scope> scopeStack = new ArrayDeque<>();

        private final Deque<Value<?>> stack = new LinkedList<>();

        public Handler(AnnotationHolder holder){
            this.holder = holder;
            scopeStack.push(new Scope(new HashMap<>(), new HashSet<>()));
        }

        public void assign(String name, Value<?> value){
            Scope scope = scopeStack.element();
            scope.values.put(name, value);
            scope.changes.add(name);
        }

        public Value<?> get(String name){
            return scopeStack.element().values.get(name);
        }

        public void enterScope(){
            Scope top = scopeStack.element();
            scopeStack.push(new Scope(new HashMap<>(top.values), new HashSet<>()));
        }

        public void leaveScope(){
            Scope top = scopeStack.remove();
            Scope newTop = scopeStack.element();
            for (String name : top.changes){
                newTop.changes.add(name);
                if (newTop.values.containsKey(name)) {
                    newTop.values.put(name, Unknown.INSTANCE);
                }
            }
        }

        public Value<?> pop(){
            if (stack.isEmpty()) return Unknown.INSTANCE;
            return stack.pop();
        }

        public void push(Value<?> value){
            stack.push(value);
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitBoolExpr(@NotNull TestBoolExpr o) {
            push(new HoldingValue<>("Boolean", Boolean.parseBoolean(o.getText())));
        }

        @Override
        public void visitIntegerExpr(@NotNull TestIntegerExpr o) {
            push(new HoldingValue<>("Integer", Integer.parseInt(o.getText())));
        }

        @Override
        public void visitRealExpr(@NotNull TestRealExpr o) {
            push(new HoldingValue<>("Real", Double.parseDouble(o.getText())));
        }

        @Override
        public void visitStringExpr(@NotNull TestStringExpr o) {
            push(new HoldingValue<>("String", o.getText().substring(1, o.getText().length() - 1)));
        }

        @Override
        public void visitNullExpr(@NotNull TestNullExpr o) {
            push(new HoldingValue<>("Null", null));
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            push(new HoldingValue<>("Function", null));
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            handleVarDec(o.getName(), o.getExpr());
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            handleVarDec(o.getName(), o.getExpr());
        }

        private void handleVarDec(String name, TestExpr expr){
            if (name == null) return;
            if (expr == null) {
                assign(name, new HoldingValue<>("Null", null));
                return;
            }
            expr.accept(this);
            assign(name, pop());
        }

        @Override
        public void visitIfElse(@NotNull TestIfElse o) {
            if (o.getExpr() != null){
                o.getExpr().accept(this);
                checkCondition(b -> {
                    String msg = b ? CONDITION_ALWAYS_TRUE : CONDITION_ALWAYS_FALSE;
                    LazyAnnotationBuilder.warningAnnotation(holder, o.getExpr(), msg).create();
                });
            }
            if (o.getStmtList().isEmpty()) return;
            enterScope();
            o.getStmtList().get(0).accept(this);
            Set<String> changes = new HashSet<>(scopeStack.remove().changes);
            if (o.getStmtList().size() < 2) return;
            enterScope();
            o.getStmtList().get(1).accept(this);
            changes.addAll(scopeStack.remove().changes);
            for (String change : changes){
                scopeStack.element().values.put(change, Unknown.INSTANCE);
                scopeStack.element().changes.add(change);
            }
        }

        @Override
        public void visitTryCatch(@NotNull TestTryCatch o) {
            if (o.getStmtList().isEmpty()) return;
            enterScope();
            o.getStmtList().get(0).accept(this);
            Set<String> changes = new HashSet<>(scopeStack.remove().changes);
            if (o.getStmtList().size() < 2) return;
            enterScope();
            if (o.getName() != null)
                assign(o.getName(), Unknown.INSTANCE);
            o.getStmtList().get(1).accept(this);
            changes.addAll(scopeStack.remove().changes);
            for (String change : changes){
                scopeStack.element().values.put(change, Unknown.INSTANCE);
                scopeStack.element().changes.add(change);
            }
        }

        @Override
        public void visitBlock(@NotNull TestBlock o) {
            enterScope();
            super.visitBlock(o);
            leaveScope();
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            enterScope();
            super.visitForLoop(o);
            leaveScope();
        }

        @Override
        public void visitWhileDo(@NotNull TestWhileDo o) {
            super.visitWhileDo(o);
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            if (o.getName() == null) push(Unknown.INSTANCE);
            Value<?> value = get(o.getName());
            if (value == null){
                PsiElement element = new TscriptDirectNavigationProvider().getNavigationElement(o);
                if (element instanceof TestFunctionDef f){
                    value = new HoldingValue<>("Function", f.getName());
                }
                else if (element instanceof TestClassDef c){
                    value = new MirrorTypeValue(new ComplexValue(c.getName()));
                }
                else if (element == null){
                    if (DefinitionCheckAnnotator.BUILT_IN_FUNCTIONS.contains(o.getName())){
                        value = new HoldingValue<>("Function", o.getName());
                    }
                    else if (DefinitionCheckAnnotator.BUILT_IN_TYPES.contains(o.getName())){
                        value = new MirrorTypeValue(new ComplexValue(o.getName()));
                    }
                    else {
                        value = Unknown.INSTANCE;
                    }
                }
                else {
                    value = Unknown.INSTANCE;
                }
            }
            push(value);
        }

        @Override
        public void visitCall(@NotNull TestCall o) {
            Value<?> value = pop();

            if (value.isOfType("Type") && value.content().equals("Type") && o.getArgList() != null){
                if (o.getArgList().getArgList().size() == 1){
                    TestArg arg = o.getArgList().getArgList().get(0);
                    arg.accept(this);
                    value = pop();
                    if (value != Unknown.INSTANCE) {
                        push(new MirrorTypeValue(value));
                        return;
                    }

                }
            }
            if (o.getArgList() == null){
                push(Unknown.INSTANCE);
                return;
            }

            for (TestArg arg : o.getArgList().getArgList()){
                arg.getExpr().accept(this);
                pop();
            }

            push(Unknown.INSTANCE);
        }

        @Override
        public void visitContainerAccess(@NotNull TestContainerAccess o) {
            pop();
            push(Unknown.INSTANCE);
        }

        @Override
        public void visitMemAccess(@NotNull TestMemAccess o) {
            pop();
            push(Unknown.INSTANCE);
        }

        @Override
        public void visitSuperAccess(@NotNull TestSuperAccess o) {
            push(Unknown.INSTANCE);
        }

        @Override
        public void visitRangeExpr(@NotNull TestRangeExpr o) {
            List<Value<?>> values = new ArrayList<>();
            for (TestExpr expr : o.getExprList()) {
                expr.accept(this);
                values.add(pop());
            }
            push(new HoldingValue<>("Range", values));
        }

        @Override
        public void visitArrayExpr(@NotNull TestArrayExpr o) {
            List<Value<?>> values = new ArrayList<>();
            for (TestExpr expr : o.getExprList()) {
                expr.accept(this);
                values.add(pop());
            }
            push(new HoldingValue<>("Array", values));
        }

        @Override
        public void visitDictionaryExpr(@NotNull TestDictionaryExpr o) {
            List<List<?>> values = new ArrayList<>();
            for (TestDictionaryEntry entry : o.getDictionaryEntryList()){
                List<Value<?>> value = new ArrayList<>();
                for (TestExpr expr : entry.getExprList()) {
                    expr.accept(this);
                    value.add(pop());
                }
                values.add(value);
            }
            push(new HoldingValue<>("Dictionary", values));
        }

        @Override
        public void visitThisExpr(@NotNull TestThisExpr o) {
            TestClassDef currClass = TscriptASTUtils.getCurrentClass(o);
            if (currClass == null || currClass.getName() == null) {
                push(Unknown.INSTANCE);
                return;
            }
            push(new ComplexValue(currClass.getName()));
        }

        @Override
        public void visitTypeofPrefixExpr(@NotNull TestTypeofPrefixExpr o) {
            o.getExpr().accept(this);
            Value<?> top = pop();
            push(new MirrorTypeValue(top));
        }

        @Override
        public void visitParam(@NotNull TestParam o) {
            if (o.getName() == null) return;
            assign(o.getName(), pop());
        }

        @Override
        public void visitNotExpr(@NotNull TestNotExpr o) {
            o.getExpr().accept(this);
            Value<?> top = pop();
            if (top.isOfType("Boolean")){
                if (top.content().equals(true)){
                    push(new HoldingValue<>("Boolean", false));
                }
                else {
                    push(new HoldingValue<>("Boolean", true));
                }
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
        }

        @Override
        public void visitEqExpr(@NotNull TestEqExpr o) {
            iterateOperation(o.getExprList(), o.getEqOpList(), (first, second, op) -> {
                if (first == Unknown.INSTANCE || second == Unknown.INSTANCE) {
                    push(Unknown.INSTANCE);
                    return;
                }

                boolean equals = op.findChildByType(TestTypes.EQUALS) != null;

                if (first.compareType(second) != equals){
                    LazyAnnotationBuilder.warningAnnotation(holder, o, EXPRESSION_ALWAYS_FALSE).create();
                    push(new HoldingValue<>("Boolean", false));
                    return;
                }

                if (first.compareContent(second) == equals){
                    LazyAnnotationBuilder.warningAnnotation(holder, o, EXPRESSION_ALWAYS_TRUE).create();
                    push(new HoldingValue<>("Boolean", true));
                }
                else {
                    LazyAnnotationBuilder.warningAnnotation(holder, o, EXPRESSION_ALWAYS_FALSE).create();
                    push(new HoldingValue<>("Boolean", false));
                }
            });
        }

        private <T extends MixinElements.Operation> void iterateOperation(List<TestExpr> expressions, List<T> operations, Callback<T> callback){
            Iterator<TestExpr> expressionIter = expressions.iterator();
            Iterator<T> operationIter = operations.iterator();

            if (!expressionIter.hasNext()) return;
            expressionIter.next().accept(this);

            Value<?> first = pop();

            while (expressionIter.hasNext() && operationIter.hasNext()){
                expressionIter.next().accept(this);

                Value<?> second = pop();
                T op = operationIter.next();

                callback.perform(first, second, op);
            }
        }

        private void checkCondition(Consumer<Boolean> callback){
            Value<?> value = pop();
            if (value == Unknown.INSTANCE) return;
            if (value.isOfType("Boolean")){
                if (value.content().equals(true)){
                    callback.accept(true);
                }
                else {
                    callback.accept(false);
                }
            }
        }
    }


    private interface Callback<O extends MixinElements.Operation> {
        void perform(Value<?> first, Value<?> second, O op);
    }

}

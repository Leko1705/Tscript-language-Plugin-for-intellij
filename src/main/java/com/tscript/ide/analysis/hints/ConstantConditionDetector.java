package com.tscript.ide.analysis.hints;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.tscript.ide.analysis.DefinitionCheckAnnotator;
import com.tscript.ide.analysis.LazyAnnotationBuilder;
import com.tscript.ide.analysis.typing.BuiltinTypes;
import com.tscript.ide.analysis.typing.Type;
import com.tscript.ide.analysis.typing.TypeBuilder;
import com.tscript.ide.analysis.typing.UnknownType;
import com.tscript.ide.psi.*;
import com.tscript.ide.reference.TscriptDirectNavigationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
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

        @NotNull Type type();

        C content();

        default boolean compareType(Value<?> value){
            return isOfType(value.type());
        }

        default boolean compareContent(Value<?> value){
            if (content() == null) return value.content() == null;
            return content().equals(value.content());
        }

        default boolean isOfType(Type type){
            return type().equals(type);
        }
    }

    private static class Unknown implements Value<Void> {

        public static final Unknown INSTANCE = new Unknown();

        @Override
        public @NotNull Type type() {
            return UnknownType.INSTANCE;
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

        @Override
        public String toString() {
            return "UnknownType.INSTANCE";
        }
    }

    private static class ComplexValue implements Value<String> {

        private final Type type;

        private ComplexValue(String typeName) {
            this.type = new TypeBuilder(typeName).create();
        }

        @Override
        public @NotNull Type type() {
            return type;
        }

        @Override
        public String  content() {
            return UUID.randomUUID().toString();
        }
    }

    private record HoldingValue<C>(@NotNull Type type, C c) implements Value<C> {

        @Override
        public C content() {
            return c;
        }
    }

    private record MirrorTypeValue(Value<?> value) implements Value<Type> {

        private static final Type TYPE = BuiltinTypes.get().get("Type");

        @Override
        public @NotNull Type type() {
            return TYPE;
        }

        @Override
        public Type content() {
            return value.type();
        }

        @Override
        public String toString() {
            return "Mirror{" + content() + "}";
        }
    }


    private static class Handler extends TestVisitor {

        private final Map<String, Type> types = BuiltinTypes.get();

        private record Scope(Map<String, Value<?>> values, Set<String> changes){}

        private final AnnotationHolder holder;

        private final Deque<Scope> scopeStack = new ArrayDeque<>();

        private final Deque<Value<?>> stack = new LinkedList<>();

        private boolean inLoop = false;

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
            push(new HoldingValue<>(types.get("Boolean"), Boolean.parseBoolean(o.getText())));
        }

        @Override
        public void visitIntegerExpr(@NotNull TestIntegerExpr o) {
            push(new HoldingValue<>(types.get("Integer"), Integer.parseInt(o.getText())));
        }

        @Override
        public void visitRealExpr(@NotNull TestRealExpr o) {
            push(new HoldingValue<>(types.get("Real"), Double.parseDouble(o.getText())));
        }

        @Override
        public void visitStringExpr(@NotNull TestStringExpr o) {
            push(new HoldingValue<>(types.get("String"), o.getText().substring(1, o.getText().length() - 1)));
        }

        @Override
        public void visitNullExpr(@NotNull TestNullExpr o) {
            push(new HoldingValue<>(types.get("Null"), null));
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            push(new HoldingValue<>(types.get("Function"), null));
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
                assign(name, new HoldingValue<>(types.get("Null"), null));
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
            if (o.getStmtList().size() == 2) {
                enterScope();
                o.getStmtList().get(1).accept(this);
                changes.addAll(scopeStack.remove().changes);
            }
            for (String change : changes){
                if (scopeStack.element().values.containsKey(change)) {
                    scopeStack.element().values.put(change, Unknown.INSTANCE);
                    scopeStack.element().changes.add(change);
                }
            }
        }

        @Override
        public void visitTryCatch(@NotNull TestTryCatch o) {
            if (o.getStmtList().isEmpty()) return;
            enterScope();
            o.getStmtList().get(0).accept(this);
            Set<String> changes = new HashSet<>(scopeStack.remove().changes);
            if (o.getStmtList().size() == 2) {
                enterScope();
                o.getStmtList().get(1).accept(this);
                changes.addAll(scopeStack.remove().changes);
            }
            enterScope();
            if (o.getName() != null)
                assign(o.getName(), Unknown.INSTANCE);
            o.getStmtList().get(1).accept(this);
            changes.addAll(scopeStack.remove().changes);
            for (String change : changes){
                if (scopeStack.element().values.containsKey(change)) {
                    scopeStack.element().values.put(change, Unknown.INSTANCE);
                    scopeStack.element().changes.add(change);
                }
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
            boolean prev = this.inLoop;
            this.inLoop = true;
            enterScope();
            if (o.getName() != null){
                assign(o.getName(), Unknown.INSTANCE);
            }
            if (o.getExpr() != null){
                o.getExpr().accept(this);
            }
            if (o.getStmt() != null)
                o.getStmt().accept(this);
            leaveScope();
            this.inLoop = prev;
        }

        @Override
        public void visitWhileDo(@NotNull TestWhileDo o) {
            boolean prev = this.inLoop;
            this.inLoop = true;
            super.visitWhileDo(o);
            this.inLoop = prev;
        }

        @Override
        public void visitDoWhile(@NotNull TestDoWhile o) {
            boolean prev = this.inLoop;
            this.inLoop = true;
            super.visitDoWhile(o);
            this.inLoop = prev;
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            if (o.getName() == null) push(Unknown.INSTANCE);
            Value<?> value = get(o.getName());
            if (value == null){
                PsiElement element = new TscriptDirectNavigationProvider().getNavigationElement(o);
                if (element instanceof TestFunctionDef f){
                    value = new HoldingValue<>(types.get("Function"), f.getName());
                }
                else if (element instanceof TestClassDef c){
                    value = new MirrorTypeValue(new ComplexValue(c.getName()));
                }
                else if (element == null){
                    if (DefinitionCheckAnnotator.BUILT_IN_FUNCTIONS.contains(o.getName())){
                        value = new HoldingValue<>(types.get("Function"), o.getName());
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

            if (value.isOfType(types.get("Type")) && value.content().equals("Type") && o.getArgList() != null){
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
            push(new HoldingValue<>(types.get("Range"), values));
        }

        @Override
        public void visitArrayExpr(@NotNull TestArrayExpr o) {
            List<Value<?>> values = new ArrayList<>();
            for (TestExpr expr : o.getExprList()) {
                expr.accept(this);
                values.add(pop());
            }
            push(new HoldingValue<>(types.get("Array"), values));
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
            push(new HoldingValue<>(types.get("Dictionary"), values));
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
            if (top.isOfType(types.get("Boolean"))){
                if (top.content().equals(true)){
                    push(new HoldingValue<>(types.get("Boolean"), false));
                }
                else {
                    push(new HoldingValue<>(types.get("Boolean"), true));
                }
            }
            else if (top.isOfType(types.get("Integer"))){
                int invert = ~((int)top.content());
                push(new HoldingValue<>(types.get("Integer"), invert));
            }
            else {
                push(Unknown.INSTANCE);
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
        }

        @Override
        public void visitAssignExpr(@NotNull TestAssignExpr o) {
            if (o.getAssignOp() == null) return;
            iterateOperation(o.getExprList(), List.of(o.getAssignOp()), (left, right, op) -> {
                if (op.findChildByType(TestTypes.ASSIGN) != null){
                    TestExpr leftEx = o.getExprList().get(0);
                    Value<?> value = right;
                    if (leftEx instanceof TestUnaryExpr u && u.getIdentifier() != null){

                        if (inLoop) value = Unknown.INSTANCE;
                        assign(u.getIdentifier().getName(), value);
                    }
                    push(value);
                }
                else if (o.getExprList().get(0) instanceof TestUnaryExpr u && u.getIdentifier() != null){

                    Value<?> result = Unknown.INSTANCE;

                    if (op.findChildByType(TestTypes.ADD_ASSIGN) != null){
                        result = evalAddOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.SUB_ASSIGN) != null){
                        result = evalSubOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.MUL_ASSIGN) != null){
                        result = evalMulOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.DIV_ASSIGN) != null){
                        result = evalDivOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.IDIV_ASSIGN) != null){
                        result = evalIdivOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.MOD_ASSIGN) != null){
                        result = evalModOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.POW_ASSIGN) != null){
                        result = evalPowOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.SAL_ASSIGN) != null){
                        result = evalSalOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.SAR_ASSIGN) != null){
                        result = evalSarOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.SLR_ASSIGN) != null){
                        result = evalSlrOp(left, right);
                    }

                    if (inLoop) result = Unknown.INSTANCE;

                    assign(u.getIdentifier().getName(), result);
                    push(result);

                }
                else {
                    push(Unknown.INSTANCE);
                }
                return true;
            });
        }

        @Override
        public void visitPlusExpr(@NotNull TestPlusExpr o) {
            iterateOperation(o.getExprList(), o.getPlusOpList(), (left, right, op) -> {

                Value<?> result = Unknown.INSTANCE;

                if (op.findChildByType(TestTypes.ADD) != null){
                    result = evalAddOp(left, right);
                }
                else if (op.findChildByType(TestTypes.SUB) != null){
                    result = evalSubOp(left, right);
                }

                push(result);
                return true;
            });
        }

        @Override
        public void visitMulExpr(@NotNull TestMulExpr o) {
            iterateOperation(o.getExprList(), o.getMulOpList(), (left, right, op) -> {

                Value<?> result = Unknown.INSTANCE;

                if (op.findChildByType(TestTypes.MUL) != null){
                    result = evalMulOp(left, right);
                }
                else if (op.findChildByType(TestTypes.DIV) != null){
                    result = evalDivOp(left, right);
                }
                else if (op.findChildByType(TestTypes.IDIV) != null){
                    result = evalIdivOp(left, right);
                }
                else if (op.findChildByType(TestTypes.MOD) != null){
                    result = evalModOp(left, right);
                }

                push(result);
                return true;
            });
        }

        @Override
        public void visitPowExpr(@NotNull TestPowExpr o) {
            iterateOperation(o.getExprList(), o.getExprList(), (left, right, op) -> {
                push(evalPowOp(left, right));
                return true;
            });
        }

        @Override
        public void visitShiftExpr(@NotNull TestShiftExpr o) {
            iterateOperation(o.getExprList(), o.getShiftOpList(), (left, right, op) -> {

                Value<?> result = Unknown.INSTANCE;

                if (op.findChildByType(TestTypes.SLR) != null){
                    result = evalSlrOp(left, right);
                }
                else if (op.findChildByType(TestTypes.SAR) != null){
                    result = evalSarOp(left, right);
                }
                else if (op.findChildByType(TestTypes.SAL) != null){
                    result = evalSalOp(left, right);
                }

                push(result);
                return true;
            });
        }


        @Override
        public void visitCompExpr(@NotNull TestCompExpr o) {
            iterateOperation(o.getExprList(), o.getCompOpList(), (left, right, op) -> {

                if (op.findChildByType(TestTypes.TYPEOF) != null){
                    if (left.type() != UnknownType.INSTANCE && right.type() != UnknownType.INSTANCE && right instanceof MirrorTypeValue v){
                        push(new HoldingValue<>(types.get("Boolean"), left.type().getName().equals(v.content().getName())));
                        return true;
                    }
                }
                else {
                    Value<?> result = Unknown.INSTANCE;

                    if (op.findChildByType(TestTypes.GT) != null){
                        result = evalGtOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.LT) != null){
                        result = evalLtOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.GEQ) != null){
                        result = evalGeqOp(left, right);
                    }
                    else if (op.findChildByType(TestTypes.LEQ) != null){
                        result = evalLeqOp(left, right);
                    }

                    push(result);
                    return false;
                }

                push(Unknown.INSTANCE);
                return true;
            });
        }

        @Override
        public void visitAndExpr(@NotNull TestAndExpr o) {
            iterateOperation(o.getExprList(), o.getExprList(), (left, right, op) -> {
                push(evalAndOp(left, right));
                return true;
            });
        }

        @Override
        public void visitOrExpr(@NotNull TestOrExpr o) {
            iterateOperation(o.getExprList(), o.getExprList(), (left, right, op) -> {
                push(evalOrOp(left, right));
                return true;
            });
        }

        @Override
        public void visitXorExpr(@NotNull TestXorExpr o) {
            iterateOperation(o.getExprList(), o.getExprList(), (left, right, op) -> {
                push(evalXorOp(left, right));
                return true;
            });
        }

        @Override
        public void visitEqExpr(@NotNull TestEqExpr o) {
            iterateOperation(o.getExprList(), o.getEqOpList(), (first, second, op) -> {
                if (first == Unknown.INSTANCE || second == Unknown.INSTANCE) {
                    push(Unknown.INSTANCE);
                    return true;
                }

                boolean equals = op.findChildByType(TestTypes.EQUALS) != null;

                if (first.compareType(second) != equals){
                    LazyAnnotationBuilder.warningAnnotation(holder, o, EXPRESSION_ALWAYS_FALSE).create();
                    push(new HoldingValue<>(types.get("Boolean"), false));
                }

                if (first.compareContent(second) == equals){
                    LazyAnnotationBuilder.warningAnnotation(holder, o, EXPRESSION_ALWAYS_TRUE).create();
                    push(new HoldingValue<>(types.get("Boolean"), true));
                }
                else {
                    LazyAnnotationBuilder.warningAnnotation(holder, o, EXPRESSION_ALWAYS_FALSE).create();
                    push(new HoldingValue<>(types.get("Boolean"), false));
                }
                return true;

            });
        }

        private <T> void iterateOperation(List<TestExpr> expressions, List<T> operations, Callback<T> callback){
            Iterator<TestExpr> expressionIter = expressions.iterator();
            Iterator<T> operationIter = operations.iterator();

            if (!expressionIter.hasNext()) return;
            expressionIter.next().accept(this);

            Value<?> first = pop();

            while (expressionIter.hasNext() && operationIter.hasNext()){
                expressionIter.next().accept(this);

                Value<?> second = pop();
                T op = operationIter.next();

                if (!callback.perform(first, second, op)){
                    break;
                }
            }
        }

        private void checkCondition(Consumer<Boolean> callback){
            Value<?> value = pop();
            if (value == Unknown.INSTANCE) return;
            if (value.isOfType(types.get("Boolean"))){
                if (value.content().equals(true)){
                    callback.accept(true);
                }
                else {
                    callback.accept(false);
                }
            }
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        private boolean canPerformOperation(
                Value<?> first,
                Value<?> second,
                boolean requireTypeMatch){

            if (first == Unknown.INSTANCE || second == Unknown.INSTANCE) {
                return false;
            }

            if (first.type() == UnknownType.INSTANCE || second.type() == UnknownType.INSTANCE) {
                return false;
            }

            if (requireTypeMatch && !first.compareType(second)) {
                return false;
            }

            return first.content() != null || second.content() == null;
        }


        private Value<?> evalNumericOp(Value<?> left,
                                       Value<?> right,
                                       BiFunction<Integer, Integer, Value<?>> intIntFunc,
                                       BiFunction<Integer, Double, Value<?>> intRealFunc,
                                       BiFunction<Double, Integer, Value<?>> realIntFunc,
                                       BiFunction<Double, Double, Value<?>> realRealFunc){

            Value<?> result = Unknown.INSTANCE;

            if (left.type() == types.get("Integer") && right.type() == types.get("Integer")){
                result = intIntFunc.apply(((int)left.content()), ((int)right.content()));
            }
            else if (left.type() == types.get("Real") && right.type() == types.get("Integer")){
                result = realIntFunc.apply(((double)left.content()), ((int)right.content()));
            }
            else if (left.type() == types.get("Integer") && right.type() == types.get("Real")){
                result = intRealFunc.apply(((int)left.content()), ((double)right.content()));
            }
            else if (left.type() == types.get("Real") && right.type() == types.get("Real")){
                result = realRealFunc.apply(((double)left.content()), ((double)right.content()));
            }

            return result;
        }

        private Value<?> evalIntBoolOp(Value<?> left,
                                       Value<?> right,
                                       BiFunction<Integer, Integer, Value<?>> intIntFunc,
                                       BiFunction<Boolean, Boolean, Value<?>> boolBoolFunc){

            Value<?> result = Unknown.INSTANCE;

            if (left.type() == types.get("Integer") && right.type() == types.get("Integer")){
                result = intIntFunc.apply(((int)left.content()), ((int)right.content()));
            }
            else if (left.type() == types.get("Boolean") && right.type() == types.get("Boolean")){
                result = boolBoolFunc.apply(((boolean)left.content()), ((boolean)right.content()));
            }

            return result;
        }

        private Value<?> evalAddOp(Value<?> left, Value<?> right) {
            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(
                    left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 + i2),
                    (i, r) -> new HoldingValue<>(types.get("Real"), ((int)left.content()) + ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) + ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) + ((double)right.content())));
        }

        private Value<?> evalSubOp(Value<?> left, Value<?> right) {
            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(
                    left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 - i2),
                    (i, r) -> new HoldingValue<>(types.get("Real"), ((int)left.content()) - ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) - ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) - ((double)right.content())));
        }

        private Value<?> evalMulOp(Value<?> left, Value<?> right) {
            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(
                    left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 * i2),
                    (i, r) -> new HoldingValue<>(types.get("Real"), ((int)left.content()) * ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) * ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) * ((double)right.content())));
        }

        private Value<?> evalDivOp(Value<?> left, Value<?> right) {
            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Real"), (double)i1 / (double)i2),
                    (i, r) -> new HoldingValue<>(types.get("Real"), ((int)left.content()) / ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) / ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Real"), ((double)left.content()) / ((double)right.content())));
        }

        private Value<?> evalPowOp(Value<?> left, Value<?> right) {
            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), (int)Math.pow(i1, i2)),
                    (i, r) -> new HoldingValue<>(types.get("Real"), Math.pow(i, r)),
                    (r, i) -> new HoldingValue<>(types.get("Real"), Math.pow(r, i)),
                    (r1, r2) -> new HoldingValue<>(types.get("Real"), Math.pow(r1, r2)));
        }


        private Value<?> evalIdivOp(Value<?> left, Value<?> right) {
            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 / i2),
                    (i, r) -> Unknown.INSTANCE,
                    (r, i) -> Unknown.INSTANCE,
                    (r1, r2) -> Unknown.INSTANCE);
        }

        private Value<?> evalModOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 % i2),
                    (i, r) -> Unknown.INSTANCE,
                    (r, i) -> Unknown.INSTANCE,
                    (r1, r2) -> Unknown.INSTANCE);
        }

        private Value<?> evalSalOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 << i2),
                    (i, r) -> Unknown.INSTANCE,
                    (r, i) -> Unknown.INSTANCE,
                    (r1, r2) -> Unknown.INSTANCE);
        }

        private Value<?> evalSarOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 >> i2),
                    (i, r) -> Unknown.INSTANCE,
                    (r, i) -> Unknown.INSTANCE,
                    (r1, r2) -> Unknown.INSTANCE);
        }

        private Value<?> evalSlrOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Integer"), i1 >>> i2),
                    (i, r) -> Unknown.INSTANCE,
                    (r, i) -> Unknown.INSTANCE,
                    (r1, r2) -> Unknown.INSTANCE);
        }

        private Value<?> evalGtOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 > i2),
                    (i, r) -> new HoldingValue<>(types.get("Boolean"), ((int)left.content()) > ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) > ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) > ((double)right.content())));
        }

        private Value<?> evalLtOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 < i2),
                    (i, r) -> new HoldingValue<>(types.get("Boolean"), ((int)left.content()) < ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) < ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) < ((double)right.content())));
        }

        private Value<?> evalGeqOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 >= i2),
                    (i, r) -> new HoldingValue<>(types.get("Boolean"), ((int)left.content()) >= ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) >= ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) >= ((double)right.content())));
        }

        private Value<?> evalLeqOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, false)){
                return Unknown.INSTANCE;
            }

            return evalNumericOp(left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 <= i2),
                    (i, r) -> new HoldingValue<>(types.get("Boolean"), ((int)left.content()) <= ((double)right.content())),
                    (r, i) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) <= ((int)right.content())),
                    (r1, r2) -> new HoldingValue<>(types.get("Boolean"), ((double)left.content()) <= ((double)right.content())));
        }

        private Value<?> evalAndOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, true)){
                return Unknown.INSTANCE;
            }

            return evalIntBoolOp(
                    left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 & i2),
                    (b1, b2) -> new HoldingValue<>(types.get("Boolean"), b1 && b2));
        }

        private Value<?> evalOrOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, true)){
                return Unknown.INSTANCE;
            }

            return evalIntBoolOp(
                    left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 | i2),
                    (b1, b2) -> new HoldingValue<>(types.get("Boolean"), b1 || b2));
        }


        private Value<?> evalXorOp(Value<?> left, Value<?> right) {

            if (!canPerformOperation(left, right, true)){
                return Unknown.INSTANCE;
            }

            return evalIntBoolOp(
                    left,
                    right,
                    (i1, i2) -> new HoldingValue<>(types.get("Boolean"), i1 ^ i2),
                    (b1, b2) -> new HoldingValue<>(types.get("Boolean"), b1 ^ b2));
        }

    }


    private interface Callback<O> {
        boolean perform(Value<?> first, Value<?> second, O op);
    }



}

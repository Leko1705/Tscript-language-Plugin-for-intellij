package com.tscript.ide.analysis;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.tscript.ide.analysis.typing.*;
import com.tscript.ide.analysis.utils.Operation;
import com.tscript.ide.analysis.utils.Visibility;
import com.tscript.ide.psi.*;
import com.tscript.ide.settings.WebSelectAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TypeCheckAnnotator implements Annotator {
    
    private static boolean targetWebTscript;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiFile file){
            PropertiesComponent properties = PropertiesComponent.getInstance(file.getProject());
            targetWebTscript = properties.getBoolean(WebSelectAction.KEY);
            
            TypeResolver typeResolver = new TypeResolver();
            file.accept(typeResolver);
            Map<String, Type> typeTable = typeResolver.typeTable;
            file.accept(new TypeChecker(holder, typeTable));
        }
    }



    private static class TypeResolver extends TestVisitor {

        private final Map<String, Type> typeTable = new HashMap<>();
        private final LinkedList<String> accessDepth = new LinkedList<>();

        public TypeResolver(){
            registerBuiltInTypes();
        }

        private void registerBuiltInTypes(){

            registerType(new TypeBuilder("Function")
                    .setCallable(true)
                    .create());

            registerType(new TypeBuilder("Type")
                    .setCallable(true)
                    .addMember(new Member("superclass", Visibility.PUBLIC, "Function", true))
                    .addMember(new Member("isOfType", Visibility.PUBLIC, "Function", true))
                    .addMember( new Member("isDerivedFrom", Visibility.PUBLIC, "Function", true))
                    .create());

            registerType(new TypeBuilder("String")
                    .setItemAccessible(true)
                    .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("find", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("split", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("toLowerCase", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("toUpperCase", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("replace", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("fromUnicode", Visibility.PUBLIC, "Function", true))
                    .addMember(new Member("join", Visibility.PUBLIC, "Function", true))
                    .create());

            registerType(new TypeBuilder("Null", "null").create());

            registerType(new TypeBuilder("Integer")
                    .addOperation(Operation.ADD, "Integer", "Integer")
                    .addOperation(Operation.ADD, "Real", "Real")
                    .addOperation(Operation.SUB, "Integer", "Integer")
                    .addOperation(Operation.SUB, "Real", "Real")
                    .addOperation(Operation.MUL, "Integer", "Integer")
                    .addOperation(Operation.MUL, "Real", "Real")
                    .addOperation(Operation.DIV, "Integer", "Real")
                    .addOperation(Operation.DIV, "Real", "Real")
                    .addOperation(Operation.IDIV, "Integer", "Integer")
                    .addOperation(Operation.MOD, "Integer", "Integer")
                    .addOperation(Operation.POW, "Integer", "Real")
                    .addOperation(Operation.POW, "Real", "Real")
                    .addOperation(Operation.SAL, "Integer", "Integer")
                    .addOperation(Operation.SAR, "Integer", "Integer")
                    .addOperation(Operation.SLR, "Integer", "Integer")
                    .addOperation(Operation.AND, "Integer", "Integer")
                    .addOperation(Operation.OR, "Integer", "Integer")
                    .addOperation(Operation.XOR, "Integer", "Integer")
                    .addOperation(Operation.GT, "Integer", "Boolean")
                    .addOperation(Operation.GT, "Real", "Boolean")
                    .addOperation(Operation.GEQ, "Integer", "Boolean")
                    .addOperation(Operation.GEQ, "Real", "Boolean")
                    .addOperation(Operation.LT, "Integer", "Boolean")
                    .addOperation(Operation.LT, "Real", "Boolean")
                    .addOperation(Operation.LEQ, "Integer", "Boolean")
                    .addOperation(Operation.LEQ, "Real", "Boolean")
                    .create());

            registerType(new TypeBuilder("Real")
                    .addOperation(Operation.ADD, "Integer", "Real")
                    .addOperation(Operation.ADD, "Real", "Real")
                    .addOperation(Operation.SUB, "Integer", "Real")
                    .addOperation(Operation.SUB, "Real", "Real")
                    .addOperation(Operation.MUL, "Integer", "Real")
                    .addOperation(Operation.MUL, "Real", "Real")
                    .addOperation(Operation.DIV, "Integer", "Real")
                    .addOperation(Operation.DIV, "Real", "Real")
                    .addOperation(Operation.POW, "Integer", "Real")
                    .addOperation(Operation.POW, "Real", "Real")
                    .addOperation(Operation.GT, "Integer", "Boolean")
                    .addOperation(Operation.GT, "Real", "Boolean")
                    .addOperation(Operation.GEQ, "Integer", "Boolean")
                    .addOperation(Operation.GEQ, "Real", "Boolean")
                    .addOperation(Operation.LT, "Integer", "Boolean")
                    .addOperation(Operation.LT, "Real", "Boolean")
                    .addOperation(Operation.LEQ, "Integer", "Boolean")
                    .addOperation(Operation.LEQ, "Real", "Boolean")
                    .addMember(new Member("isFinite", Visibility.PUBLIC, "Function", true))
                    .addMember(new Member("isInfinite", Visibility.PUBLIC, "Function", true))
                    .addMember(new Member("isNan", Visibility.PUBLIC, "Function", true))
                    .addMember(new Member("inf", Visibility.PUBLIC, "Function", true))
                    .addMember(new Member("nan", Visibility.PUBLIC, "Function", true))
                    .create());

            registerType(new TypeBuilder("Boolean")
                    .addOperation(Operation.AND, "Boolean", "Boolean")
                    .addOperation(Operation.OR, "Boolean", "Boolean")
                    .addOperation(Operation.XOR, "Boolean", "Boolean")
                    .create());

            registerType(new TypeBuilder("Range")
                    .setItemAccessible(true)
                    .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("begin", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("end", Visibility.PUBLIC, "Function", false))
                    .create());

            registerType(new TypeBuilder("Array")
                    .setItemAccessible(true)
                    .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("slice", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("push", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("pop", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("insert", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("remove", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("sort", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("keys", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("values", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("concat", Visibility.PUBLIC, "Function", true))
                    .create());

            registerType(new TypeBuilder("Dictionary")
                    .setItemAccessible(true)
                    .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("has", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("remove", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("keys", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("values", Visibility.PUBLIC, "Function", false))
                    .addMember(new Member("merge", Visibility.PUBLIC, "Function", true))
                    .create());

        }

        private void registerType(Type type){
            typeTable.put(type.getName(), type);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            accessDepth.addLast(o.getName());

            StringBuilder fullName = new StringBuilder();
            Iterator<String> itr = accessDepth.iterator();
            fullName.append(itr.next());
            while (itr.hasNext()) fullName.append(".").append(itr.next());

            TypeBuilder builder = new TypeBuilder(fullName.toString());
            builder.setCallable(false).setItemAccessible(false);

            if (o.getClassBodyDef() != null) {
                for (PsiElement element : o.getClassBodyDef().getChildren()) {

                    element.accept(new TestVisitor() {
                        Visibility currentVisibility = Visibility.PUBLIC;
                        boolean isStatic = false;

                        @Override
                        public void visitClassDef(@NotNull TestClassDef o) {
                            TypeResolver.this.visitClassDef(o);
                            builder.addMember(new Member(o.getName(), currentVisibility, "Type", o.getStaticElement() != null));
                        }

                        @Override
                        public void visitFunctionDef(@NotNull TestFunctionDef o) {
                            builder.addMember(new Member(o.getName(), currentVisibility, "Function", o.getStaticElement() != null));
                        }

                        @Override
                        public void visitVarDec(@NotNull TestVarDec o) {
                            isStatic = o.getStaticElement() != null;
                            o.acceptChildren(this);
                            isStatic = false;
                        }

                        @Override
                        public void visitConstDec(@NotNull TestConstDec o) {
                            isStatic = o.getStaticElement() != null;
                            o.acceptChildren(this);
                            isStatic = false;
                        }

                        @Override
                        public void visitSingleVar(@NotNull TestSingleVar o) {
                            builder.addMember(new Member(o.getName(), currentVisibility, m -> UnknownType.INSTANCE, isStatic));
                        }

                        @Override
                        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                            accessDepth.addLast(o.getName());
                            o.acceptChildren(this);
                        }

                        @Override
                        public void visitVisibility(@NotNull TestVisibility o) {
                            currentVisibility = switch (Objects.requireNonNull(o.getName())) {
                                case "public" -> Visibility.PUBLIC;
                                case "private" -> Visibility.PRIVATE;
                                case "protected" -> Visibility.PROTECTED;
                                default -> {
                                    if (o.getName() != null) throw new IllegalStateException();
                                    yield null;
                                }
                            };
                        }
                    });
                }
            }

            if (o.getSuper() != null){
                fullName = new StringBuilder();
                Iterator<TestIdentifier> supItr = o.getSuper().getIdentifierList().iterator();
                fullName.append(supItr.next());
                while (supItr.hasNext()) fullName.append(".").append(supItr.next().getName());
                builder.setSuperType(fullName.toString());
            }

            o.acceptChildren(this);
            accessDepth.removeLast();
            registerType(builder.create());
        }

    }
    
    
    

    private static class TypeChecker extends TestVisitor {

        private final AnnotationHolder holder;
        private final Map<String, Type> typeTable;
        private final Deque<TypeChecker.CheckScope> scopeStack = new ArrayDeque<>();

        private record CheckScope(Deque<Type> opStack, Map<String, Type> varTypes, Set<String> changes){
            public CheckScope(){
                this(new ArrayDeque<>(), new HashMap<>(), new HashSet<>());
            }
        }

        private void pushType(Type type){
            scopeStack.element().opStack.push(type);
        }

        private Type popType(){
            return scopeStack.element().opStack.pop();
        }

        private boolean typeAvailable(){
            return !scopeStack.element().opStack.isEmpty();
        }

        private void setVariableType(String name, Type type){
            scopeStack.element().varTypes.put(name, type);
            scopeStack.element().changes.add(name);
        }

        private Type getVariableType(String name){
            Type type = scopeStack.element().varTypes.get(name);
            if (type == null) return UnknownType.INSTANCE;
            return type;
        }

        private void enterBlockScope(){
            TypeChecker.CheckScope top = scopeStack.element();
            TypeChecker.CheckScope newScope = new TypeChecker.CheckScope(new ArrayDeque<>(), top.varTypes, new HashSet<>());
            scopeStack.push(newScope);
        }

        private void leaveBlockScope(){
            TypeChecker.CheckScope top = scopeStack.pop();
            TypeChecker.CheckScope newTop = scopeStack.element();
            for (String changed : top.changes){
                if (newTop.varTypes.containsKey(changed)){
                    newTop.varTypes.put(changed, UnknownType.INSTANCE);
                }
            }
        }

        private TypeChecker(AnnotationHolder holder, Map<String, Type> typeTable) {
            this.holder = holder;
            this.typeTable = typeTable;
        }

        @Override
        public void visitPsiElement(@NotNull PsiElement o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            scopeStack.push(new TypeChecker.CheckScope());
            file.acceptChildren(this);
            scopeStack.pop();
        }

        @Override
        public void visitUnaryExpr(@NotNull TestUnaryExpr o) {
            super.visitUnaryExpr(o);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            scopeStack.push(new TypeChecker.CheckScope());
            o.acceptChildren(this);
            scopeStack.pop();
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            scopeStack.push(new TypeChecker.CheckScope());
            o.acceptChildren(this);
            scopeStack.pop();
        }

        @Override
        public void visitParam(@NotNull TestParam o) {
            setVariableType(o.getName(), UnknownType.INSTANCE);
        }

        @Override
        public void visitBlock(@NotNull TestBlock o) {
            enterBlockScope();
            o.acceptChildren(this);
            leaveBlockScope();
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            enterBlockScope();
            o.acceptChildren(this);
            leaveBlockScope();
            pushType(typeTable.get("Function"));
        }

        @Override
        public void visitWhileDo(@NotNull TestWhileDo o) {
            if (o.getExpr() != null) {
                o.getExpr().accept(this);
                requireType("Boolean", o.getExpr());
            }

            enterBlockScope();
            if (o.getStmt() != null)
                o.getStmt().accept(this);
            leaveBlockScope();
        }

        @Override
        public void visitImportStmt(@NotNull TestImportStmt o) {
            if (o.getChainableIdentifier() == null) return;
            List<TestIdentifier> list = o.getChainableIdentifier().getIdentifierList();
            setVariableType(list.get(list.size()-1).getName(), UnknownType.INSTANCE);
        }

        @Override
        public void visitFromImport(@NotNull TestFromImport o) {
            if (o.getChainableIdentifierList().size() != 2) return;
            List<TestIdentifier> list = o.getChainableIdentifierList().get(1).getIdentifierList();
            setVariableType(list.get(list.size()-1).getName(), UnknownType.INSTANCE);
        }

        @Override
        public void visitDoWhile(@NotNull TestDoWhile o) {
            enterBlockScope();
            if (o.getStmt() != null)
                o.getStmt().accept(this);

            if (o.getExpr() != null) {
                o.getExpr().accept(this);
                requireType("Boolean", o.getExpr());
            }
            leaveBlockScope();
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            enterBlockScope();
            if (o.getName() != null){
                setVariableType(o.getName(), UnknownType.INSTANCE);
            }
            if (o.getExpr() != null){
                o.getExpr().accept(this);
                requireIterable(o.getExpr());
            }
            if (o.getStmt() != null)
                o.getStmt().accept(this);
            leaveBlockScope();
        }

        @Override
        public void visitTryCatch(@NotNull TestTryCatch o) {
            enterBlockScope();
            if (!o.getStmtList().isEmpty()){
                o.getStmtList().get(0).accept(this);
                if (o.getStmtList().size() == 2){
                    if (o.getName() != null){
                        setVariableType(o.getName(), UnknownType.INSTANCE);
                    }
                    o.getStmtList().get(1).accept(this);
                }
            }
            leaveBlockScope();
        }

        @Override
        public void visitIfElse(@NotNull TestIfElse o) {
            if (o.getExpr() != null) {
                o.getExpr().accept(this);
                requireType("Boolean", o.getExpr());
            }

            if (o.getStmtList().size() == 1){
                // only if
                enterBlockScope();
                o.getStmtList().get(0).accept(this);
                leaveBlockScope();
            }
            else if (o.getStmtList().size() > 1){
                // if and else
                TypeChecker.CheckScope top = scopeStack.element();

                TypeChecker.CheckScope ifScope = new TypeChecker.CheckScope(new ArrayDeque<>(), top.varTypes, new HashSet<>());
                scopeStack.push(ifScope);
                o.getStmtList().get(0).acceptChildren(this);
                scopeStack.pop();

                TypeChecker.CheckScope elseScope = new TypeChecker.CheckScope(new ArrayDeque<>(), top.varTypes, new HashSet<>());
                scopeStack.push(elseScope);
                o.getStmtList().get(1).acceptChildren(this);
                scopeStack.pop();

                Set<String> allChanges = new HashSet<>(ifScope.changes);
                allChanges.addAll(elseScope.changes);

                for (String changed : allChanges){
                    if (top.varTypes.containsKey(changed)){
                        top.varTypes.put(changed, UnknownType.INSTANCE);
                    }
                }

            }

        }

        private void requireType(String expectedTypeName, PsiElement caller){
            if (!typeAvailable()) return;
            Type type = popType();
            if (type != UnknownType.INSTANCE && !type.getName().equals(expectedTypeName)){
                LazyAnnotationBuilder.errorAnnotator(holder, caller, true, "type mismatch:\nrequired: "+ expectedTypeName + "\ngot: " + type.getPrintName()).create();
            }
        }

        private void requireIterable(PsiElement caller){
            if (!typeAvailable()) return;
            Type type = popType();
            if (!type.canItemAccess()){
                LazyAnnotationBuilder.errorAnnotator(holder, caller, true, "type mismatch: " + type.getPrintName() + " is not iterable").create();
            }
        }

        @Override
        public void visitEqExpr(@NotNull TestEqExpr o) {

            if (!o.getExprList().isEmpty()) {
                o.getExprList().get(0).accept(this);
                if (typeAvailable()) popType();

                if (o.getExprList().size() == 2) {
                    o.getExprList().get(1).accept(this);
                    if (typeAvailable()) popType();
                }
            }

            pushType(typeTable.get("Boolean"));
        }

        @Override
        public void visitContainerAccess(@NotNull TestContainerAccess o) {

            Type accessedType = null;

            if (typeAvailable()) {
                accessedType = popType();
                if (!accessedType.canItemAccess()) {
                    LazyAnnotationBuilder.errorAnnotator(holder, o, true, "type mismatch: can not access " + accessedType.getPrintName()).create();
                }
            }

            if (o.getExpr() == null){
                pushType(UnknownType.INSTANCE);
                return;
            }


            o.getExpr().accept(this);

            if (!typeAvailable()){
                pushType(UnknownType.INSTANCE);
                return;
            }

            Type keyType = popType();

            if (accessedType == null){
                pushType(UnknownType.INSTANCE);
                return;
            }

            if (keyType == UnknownType.INSTANCE){
                pushType(UnknownType.INSTANCE);
                return;
            }

            String keyTypeName = keyType.getName();

            Type type = switch (accessedType.getName()) {
                case "String" -> {
                    if (!keyTypeName.equals("Integer") && !keyTypeName.equals("Range")) {
                        LazyAnnotationBuilder.errorAnnotator(holder, o.getExpr(), true, "type mismatch: can not access String with " + keyType.getPrintName()).create();
                        yield UnknownType.INSTANCE;
                    }

                    yield typeTable.get("String");
                }
                case "Array" -> {
                    if (keyTypeName.equals("Integer")) {
                        yield UnknownType.INSTANCE;
                    }
                    else if (keyTypeName.equals("Range")) {
                        yield accessedType; // push sub-array (array-type)
                    }

                    LazyAnnotationBuilder.errorAnnotator(holder, o.getExpr(), true, "type mismatch: can not access Array with " + keyType.getPrintName()).create();
                    yield UnknownType.INSTANCE;

                }
                case "Range" -> {
                    if (keyTypeName.equals("Integer") || keyTypeName.equals("Range")) {
                        // for an integer-key or a range-key the result type is always the same as Integer/Range
                        yield keyType;
                    }

                    LazyAnnotationBuilder.errorAnnotator(holder, o.getExpr(), true, "type mismatch: can not access Range with " + keyType.getPrintName()).create();
                    yield UnknownType.INSTANCE;
                }
                case "Dictionary" -> {
                    if (targetWebTscript && !keyTypeName.equals("String")) {
                        LazyAnnotationBuilder.errorAnnotator(holder, o.getExpr(), true, "type mismatch: can not access Dictionary with " + keyType.getPrintName()).create();
                    }
                    yield UnknownType.INSTANCE;
                }

                default -> UnknownType.INSTANCE;
            };

            pushType(type);
        }

        @Override
        public void visitIntegerExpr(@NotNull TestIntegerExpr o) {
            pushType(typeTable.get("Integer"));
        }

        @Override
        public void visitRealExpr(@NotNull TestRealExpr o) {
            pushType(typeTable.get("Real"));
        }

        @Override
        public void visitNullExpr(@NotNull TestNullExpr o) {
            pushType(typeTable.get("Null"));
        }

        @Override
        public void visitStringExpr(@NotNull TestStringExpr o) {
            pushType(typeTable.get("String"));
        }

        @Override
        public void visitBoolExpr(@NotNull TestBoolExpr o) {
            pushType(typeTable.get("Boolean"));
        }

        @Override
        public void visitPlusExpr(@NotNull TestPlusExpr o) {
            defaultHandleMultiBinary(o, o.getExprList(), o.getPlusOpList());
        }

        @Override
        public void visitMulExpr(@NotNull TestMulExpr o) {
            defaultHandleMultiBinary(o, o.getExprList(), o.getMulOpList());
        }

        @Override
        public void visitShiftExpr(@NotNull TestShiftExpr o) {
            defaultHandleMultiBinary(o, o.getExprList(), o.getShiftOpList());
        }

        @Override
        public void visitCompExpr(@NotNull TestCompExpr o) {
            defaultHandleMultiBinary(o, o.getExprList(), o.getCompOpList());
        }

        @Override
        public void visitAndExpr(@NotNull TestAndExpr o) {
            handleBinary(o.getExprList(), (l, r, op) -> checkBinaryType(o, l, r, Operation.AND));
        }

        @Override
        public void visitOrExpr(@NotNull TestOrExpr o) {
            handleBinary(o.getExprList(), (l, r, op) -> checkBinaryType(o, l, r, Operation.OR));
        }

        @Override
        public void visitXorExpr(@NotNull TestXorExpr o) {
            handleBinary(o.getExprList(), (l, r, op) -> checkBinaryType(o, l, r, Operation.XOR));
        }

        @Override
        public void visitArrayExpr(@NotNull TestArrayExpr o) {
            for (TestExpr elem : o.getExprList()){
                elem.accept(this);
                if (typeAvailable()) popType();
            }
            pushType(typeTable.get("Array"));
        }

        @Override
        public void visitDictionaryExpr(@NotNull TestDictionaryExpr o) {
            o.acceptChildren(this);
            pushType(typeTable.get("Dictionary"));
        }

        @Override
        public void visitDictionaryEntry(@NotNull TestDictionaryEntry o) {

            if (!o.getExprList().isEmpty()) {
                o.getExprList().get(0).accept(this);
                if (targetWebTscript) {
                    requireType("String", o.getExprList().get(0));
                }
                else {
                    if (typeAvailable()) popType();
                }

                if (o.getExprList().size() == 2) {
                    o.getExprList().get(1).accept(this);
                    if (typeAvailable()) popType();
                }
            }
        }

        @Override
        public void visitRangeExpr(@NotNull TestRangeExpr o) {

            if (!o.getExprList().isEmpty()) {
                o.getExprList().get(0).accept(this);
                requireType("Integer", o.getExprList().get(0));

                if (o.getExprList().size() == 2) {
                    o.getExprList().get(1).accept(this);
                    requireType("Integer", o.getExprList().get(1));
                }
            }

            pushType(typeTable.get("Range"));
        }

        @Override
        public void visitAssignExpr(@NotNull TestAssignExpr o) {
            if (o.getAssignOp() == null){
                o.acceptChildren(this);
                return;
            }

            if (o.getAssignOp().findChildByType(TestTypes.ASSIGN) != null){
                List<TestExpr> list = o.getExprList();
                if (list.get(0) instanceof TestUnaryExpr u && u.getIdentifier() != null && list.size() == 2){
                    list.get(1).accept(this);
                    if (!typeAvailable()) {
                        pushType(UnknownType.INSTANCE);
                        return;
                    }
                    Type type = popType();
                    setVariableType(u.getIdentifier().getName(), type);
                    pushType(type);
                }

                return;
            }

            defaultHandleMultiBinary(o, o.getExprList(), List.of(o.getAssignOp()));

            List<TestExpr> expression = o.getExprList();
            if (!expression.isEmpty() && expression.get(0) instanceof TestUnaryExpr u){

                if (u.getIdentifier() != null)
                    scopeStack.element().changes.add(u.getIdentifier().getName());
            }
        }

        @Override
        public void visitNotExpr(@NotNull TestNotExpr o) {
            o.getExpr().accept(this);
            if (!typeAvailable()) {
                pushType(UnknownType.INSTANCE);
                return;
            }

            Type type = popType();
            if (type != UnknownType.INSTANCE && !Set.of("Integer", "Boolean").contains(type.getName())){
                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "type mismatch: cannot invert " + type.getPrintName()).create();
                pushType(UnknownType.INSTANCE);
            }
            else {
                pushType(type);
            }
        }

        @Override
        public void visitNegationExpr(@NotNull TestNegationExpr o) {
            o.getExpr().accept(this);
            if (!typeAvailable()) {
                pushType(UnknownType.INSTANCE);
                return;
            }

            Type type = popType();
            if (type != UnknownType.INSTANCE && !Set.of("Integer", "Real").contains(type.getName())){
                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "type mismatch: cannot negate " + type.getPrintName()).create();
                pushType(UnknownType.INSTANCE);
            }
            else {
                pushType(type);
            }
        }

        @Override
        public void visitTypeofPrefixExpr(@NotNull TestTypeofPrefixExpr o) {
            if (targetWebTscript){
                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "web tscript does not support unary typeof operator").create();
            }

            o.getExpr().accept(this);
            if (!typeAvailable()) return;
            Type top = popType();
            if (top == UnknownType.INSTANCE) {
                pushType(UnknownType.INSTANCE);
                return;
            }

            if (top instanceof MirrorType){
                pushType(typeTable.get("Type"));
            }
            else {
                pushType(new MirrorType(top));
            }
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            handleDefinition(o.getName(), o.getExpr());
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            handleDefinition(o.getName(), o.getExpr());
        }

        private void handleDefinition(String name, PsiElement exp){
            if (exp == null){
                pushType(typeTable.get("Null"));
            }
            else {
                exp.accept(this);
            }

            Type type;
            if (typeAvailable()) type = popType();
            else type = UnknownType.INSTANCE;

            setVariableType(name, type);
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            pushType(getVariableType(o.getName()));
        }

        private void checkBinaryType(PsiElement caller, Type l, Type r, Operation op){
            Type returnType = l.operate(op, r, typeTable);
            if (returnType == null){
                LazyAnnotationBuilder.errorAnnotator(holder, caller, true, "type mismatch: cannot perform " + op.name + " on " + l.getPrintName() + " and " + r.getPrintName()).create();
                pushType(UnknownType.INSTANCE);
            }
            else {
                pushType(returnType);
            }
        }

        private void defaultHandleMultiBinary(PsiElement caller,
                                              List<? extends PsiElement> expressions,
                                              List<? extends PsiElement> operands){
            handleMultiBinary(expressions, operands, (l, r, op) -> checkBinaryType(caller, l, r, op));
        }

        private void handleBinary(List<? extends PsiElement> expressions,
                                  TypeChecker.BinaryOperationHandler handler){

            if (expressions.isEmpty()){
                pushType(UnknownType.INSTANCE);
                return;
            }

            Type left = null;

            for (PsiElement operand : expressions) {
                operand.accept(this);

                if (!typeAvailable()){
                    pushType(UnknownType.INSTANCE);
                    return;
                }
                Type right = popType();

                if (left == null){
                    left = right;
                    continue;
                }

                handler.handle(left, right, null);
                left = right;
            }

            pushType(left);
        }

        private void handleMultiBinary(List<? extends PsiElement> expressions,
                                       List<? extends PsiElement> operations,
                                       TypeChecker.BinaryOperationHandler handler){

            if (expressions.isEmpty()){
                pushType(UnknownType.INSTANCE);
                return;
            }

            if (operations.isEmpty()){
                expressions.get(0).accept(this);
                return;
            }

            Type left = null;
            Iterator<? extends PsiElement> operationItr = operations.iterator();

            for (PsiElement operand : expressions) {
                operand.accept(this);

                if (!typeAvailable()){
                    pushType(UnknownType.INSTANCE);
                    return;
                }
                Type right = popType();

                if (left == null){
                    left = right;
                    continue;
                }

                if (!operationItr.hasNext()){
                    pushType(left);
                    return;
                }

                PsiElement operation = operationItr.next();
                Operation[] op = new Operation[1];

                operation.accept(new TestVisitor(){

                    @Override
                    public void visitPlusOp(@NotNull TestPlusOp o) {
                        if (o.findChildByType(TestTypes.ADD) != null){
                            op[0] = Operation.ADD;
                        }
                        else if (o.findChildByType(TestTypes.SUB) != null){
                            op[0] = Operation.SUB;
                        }
                    }

                    @Override
                    public void visitMulOp(@NotNull TestMulOp o) {
                        if (o.findChildByType(TestTypes.MUL) != null){
                            op[0] = Operation.MUL;
                        }
                        else if (o.findChildByType(TestTypes.DIV) != null){
                            op[0] = Operation.DIV;
                        }
                        else if (o.findChildByType(TestTypes.IDIV) != null){
                            op[0] = Operation.IDIV;
                        }
                        else if (o.findChildByType(TestTypes.MOD) != null){
                            op[0] = Operation.MOD;
                        }
                    }

                    private void reportUnsupportedShift(PsiElement o){
                        LazyAnnotationBuilder.errorAnnotator(holder, o, true, "web tscript does not support shift operator").create();
                    }

                    @Override
                    public void visitShiftOp(@NotNull TestShiftOp o) {
                        if (targetWebTscript)
                            reportUnsupportedShift(o);

                        if (o.findChildByType(TestTypes.SAL) != null){
                            op[0] = Operation.SAL;
                        }
                        else if (o.findChildByType(TestTypes.SAR) != null){
                            op[0] = Operation.SAR;
                        }
                        else if (o.findChildByType(TestTypes.SLR) != null){
                            op[0] = Operation.SLR;
                        }
                    }

                    @Override
                    public void visitCompOp(@NotNull TestCompOp o) {
                        if (o.findChildByType(TestTypes.GT) != null){
                            op[0] = Operation.GT;
                        }
                        else if (o.findChildByType(TestTypes.GEQ) != null){
                            op[0] = Operation.GEQ;
                        }
                        else if (o.findChildByType(TestTypes.LT) != null){
                            op[0] = Operation.LT;
                        }
                        else if (o.findChildByType(TestTypes.LEQ) != null){
                            op[0] = Operation.LEQ;
                        }
                        else if (o.findChildByType(TestTypes.TYPEOF) != null && targetWebTscript){
                            LazyAnnotationBuilder.errorAnnotator(holder, o, true, "web tscript does not support binary typeof operator").create();
                        }
                    }

                    @Override
                    public void visitAssignOp(@NotNull TestAssignOp o) {
                        if (o.findChildByType(TestTypes.ADD_ASSIGN) != null){
                            op[0] = Operation.ADD;
                        }
                        else if (o.findChildByType(TestTypes.SUB_ASSIGN) != null){
                            op[0] = Operation.SUB;
                        }
                        else if (o.findChildByType(TestTypes.MUL_ASSIGN) != null){
                            op[0] = Operation.MUL;
                        }
                        else if (o.findChildByType(TestTypes.DIV_ASSIGN) != null){
                            op[0] = Operation.DIV;
                        }
                        else if (o.findChildByType(TestTypes.IDIV_ASSIGN) != null){
                            op[0] = Operation.IDIV;
                        }
                        else if (o.findChildByType(TestTypes.MOD_ASSIGN) != null){
                            op[0] = Operation.MOD;
                        }
                        else if (o.findChildByType(TestTypes.POW_ASSIGN) != null){
                            op[0] = Operation.POW;
                        }
                        else if (o.findChildByType(TestTypes.SAL_ASSIGN) != null){
                            if (targetWebTscript)
                                reportUnsupportedShift(o);
                            op[0] = Operation.SAL;
                        }
                        else if (o.findChildByType(TestTypes.SAR_ASSIGN) != null){
                            if (targetWebTscript)
                                reportUnsupportedShift(o);
                            op[0] = Operation.SAR;
                        }
                        else if (o.findChildByType(TestTypes.SLR_ASSIGN) != null){
                            if (targetWebTscript)
                                reportUnsupportedShift(o);
                            op[0] = Operation.SLR;
                        }
                    }
                });

                if (op[0] == null)
                    return;

                handler.handle(left, right, op[0]);
                left = right;
            }
        }

        interface BinaryOperationHandler {
            void handle(Type left, Type right, Operation operation);
        }

    }

}

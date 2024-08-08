package com.test.language.highlight;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.test.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

final class TestAnnotator implements Annotator {

    private static final Set<String> BUILT_IN_FUNCTIONS = Set.of("print", "exit", "error", "assert");
    private static final Set<String> BUILT_IN_TYPES =
            Set.of("Integer", "Real", "Boolean", "String", "Null", "Array", "Dictionary", "Range", "Function", "Type");



    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

        if (element instanceof PsiFile file){
            performAction(file, holder);
        }

    }

    private void performAction(PsiFile file, AnnotationHolder holder){

        SymbolResolver symbolResolver = new SymbolResolver();
        file.accept(symbolResolver);
        Table table = new Table(symbolResolver.scope, symbolResolver.nodeTable);

        HierarchyResolver hierarchyResolver = new HierarchyResolver(table);
        file.accept(hierarchyResolver);
        Hierarchy hierarchy = hierarchyResolver.hierarchy;

        file.accept(new DefinitionChecker(table, hierarchy));
        file.accept(new DependencyChecker(table, hierarchy));
        file.accept(new ScopeChecker(table));
        file.accept(new TypeChecker());
        file.accept(new FlowAnalyzer(table));

        for (PsiElement e : table.nodeTable.keySet()){
            PsiElementInfo info = table.nodeTable.get(e);
            if (info.message != null){
                info.message.apply(info, holder);
            }
            else if (!info.attributes.isEmpty() && info.element != null){
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(info.element)
                        .textAttributes(Styles.mergeAttributes(info.attributes.toArray(new TextAttributesKey[0])))
                        .create();
            }
        }
    }


    private static class SymbolResolver extends TestVisitor {

        public Scope scope;
        public Map<PsiElement, PsiElementInfo> nodeTable = new HashMap<>();

        @Override
        public void visitFile(@NotNull PsiFile file) {
            scope = new Scope(Scope.Kind.GLOBAL, file);

            // LOAD BUILT-INS
            BUILT_IN_FUNCTIONS.forEach(func -> putIfAbsent(scope, func, file, Symbol.Kind.FUNCTION, null));
            BUILT_IN_TYPES.forEach(func -> putIfAbsent(scope, func, file, Symbol.Kind.CLASS, null));

            file.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            putIfAbsent(scope, o.getName(), o.getNameIdentifier(), Symbol.Kind.CLASS, TestSyntaxHighlighter.CLASS_DEF_NAME);
            Scope previous = scope;
            scope = new Scope(Scope.Kind.CLASS, scope, o);
            previous.children.put(o, scope);
            o.acceptChildren(this);
            scope = previous;
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            putIfAbsent(scope, o.getName(), o.getNameIdentifier(), Symbol.Kind.NAMESPACE, null);
            Scope previous = scope;
            scope = new Scope(Scope.Kind.NAMESPACE, scope, o);
            previous.children.put(o, scope);
            o.acceptChildren(this);
            scope = previous;
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            putIfAbsent(scope, o.getName(), o.getNameIdentifier(), Symbol.Kind.FUNCTION, TestSyntaxHighlighter.FUNC_DEF_NAME);
            Scope previous = scope;
            scope = new Scope(Scope.Kind.FUNCTION, scope, o);
            previous.children.put(o, scope);
            o.acceptChildren(this);
            scope = previous;
        }

        @Override
        public void visitConstructorDef(@NotNull TestConstructorDef o) {
            Scope previous = scope;
            scope = new Scope(Scope.Kind.CONSTRUCTOR, scope, o);
            previous.children.put(o, scope);
            o.acceptChildren(this);
            scope = previous;
        }

        @Override
        public void visitBlock(@NotNull TestBlock o) {
            Scope previous = scope;
            scope = new Scope(Scope.Kind.BLOCK, scope, o);
            previous.children.put(o, scope);
            o.acceptChildren(this);
            scope = previous;
        }

        @Override
        public void visitVarDec(@NotNull TestVarDec o) {
            TextAttributesKey key = null;
            if (scope.kind == Scope.Kind.CLASS) key = TestSyntaxHighlighter.MEMBER_REF_NAME;
            for (TestSingleVar s : o.getSingleVarList()) {
                putIfAbsent(scope, s.getName(), s.getNameIdentifier(), Symbol.Kind.VARIABLE, key);
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            TextAttributesKey key = null;
            if (scope.kind == Scope.Kind.CLASS) key = TestSyntaxHighlighter.MEMBER_REF_NAME;
            for (TestSingleConst s : o.getSingleConstList())
                putIfAbsent(scope, s.getName(), s, Symbol.Kind.CONSTANT, key);
        }

        @Override
        public void visitParam(@NotNull TestParam o) {
            putIfAbsent(scope, o.getName(), o.getNameIdentifier(), Symbol.Kind.VARIABLE, null);
        }

        private void putIfAbsent(Scope scope, String name, PsiElement element, Symbol.Kind kind, TextAttributesKey extraKey){
            Scope curr = scope;
            Set<TextAttributesKey> keys =
                    extraKey != null
                    ? new HashSet<>(Set.of(extraKey))
                    : new HashSet<>();

            do {
                if (curr.table.containsKey(name)){
                    Fix fix = new Fix(new LinkToDuplicateFix(curr.table.get(name).element), "");
                    ErrorMessage msg = new ErrorMessage("'" + name + "' already exist", fix);
                    keys.add(TestSyntaxHighlighter.ERROR_UNDERLINE);
                    nodeTable.put(element, new PsiElementInfo(element, msg, keys));
                    return;
                }

                if (curr.parent != null && (curr.kind == Scope.Kind.BLOCK || curr.kind == Scope.Kind.GLOBAL)) {
                    curr = curr.parent;
                    continue;
                }

                break;
            }
            while (true);

            scope.table.put(name, new Symbol(name, element, kind, scope.kind));
            nodeTable.put(element, new PsiElementInfo(element, null, keys));
        }
    }


    private static class HierarchyResolver extends TestVisitor {

        private Hierarchy.Layer layer = new Hierarchy.Layer(null);
        public Hierarchy hierarchy;

        private final Table table;

        private HierarchyResolver(Table table) {
            this.table = table;
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            table.moveTopLevel();
            file.acceptChildren(this);
            hierarchy = new Hierarchy(layer);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            expandHierarchy(o);
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            expandHierarchy(o);
        }

        private void expandHierarchy(PsiNameIdentifierOwner o){
            Hierarchy.Layer newLayer = new Hierarchy.Layer(table.search(scope -> scope.table.get(o.getName())));
            layer.subLayers.put(o.getName(), newLayer);
            Hierarchy.Layer prev = layer;
            layer = newLayer;
            table.enterScope(o);
            o.acceptChildren(this);
            layer = prev;
            table.leaveScope();
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
        }
    }


    private static class DefinitionChecker extends TestVisitor {

        private final Table table;
        private final Hierarchy hierarchy;
        private final Deque<TestClassDef> classStack = new ArrayDeque<>();

        private DefinitionChecker(Table table, Hierarchy hierarchy) {
            this.table = table;
            this.hierarchy = hierarchy;
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
            table.moveTopLevel();
            file.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            table.enterScope(o);
            classStack.push(o);
            o.acceptChildren(this);
            table.leaveScope();
            classStack.remove();
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier u) {
            String name = u.getName();
            Symbol symbol = table.search(s -> s.table.get(name));
            AtomicBoolean found = new AtomicBoolean(symbol != null);

            if (symbol == null && !classStack.isEmpty()){
                TestClassDef def = classStack.element();

                while (def.getSuper() != null) {
                    List<String> list = new ArrayList<>();
                    for (TestIdentifier ident : def.getSuper().getIdentifierList()) {
                        list.add(ident.getName());
                    }

                    Symbol sym = hierarchy.search(list);

                    if (sym != null) {
                        def = (TestClassDef) sym.element.getParent();
                        def.acceptChildren(new TestVisitor() {
                            @Override
                            public void visitElement(@NotNull PsiElement element) {
                                element.acceptChildren(this);
                            }

                            @Override
                            public void visitPsiElement(@NotNull PsiElement o) {
                                o.acceptChildren(this);
                            }

                            @Override
                            public void visitSingleVar(@NotNull TestSingleVar o) {
                                if (o.getName() != null && o.getName().equals(name)) {
                                    found.set(true);
                                    table.nodeTable.put(u,
                                            new PsiElementInfo(
                                                    u,
                                                    null,
                                                    Set.of(TestSyntaxHighlighter.MEMBER_REF_NAME)
                                            ));
                                }
                            }

                            @Override
                            public void visitSingleConst(@NotNull TestSingleConst o) {
                                if (o.getName() != null && o.getName().equals(name)) {
                                    found.set(true);
                                    table.nodeTable.put(u,
                                            new PsiElementInfo(
                                                    u,
                                                    null,
                                                    Set.of(TestSyntaxHighlighter.MEMBER_REF_NAME)
                                            ));
                                }
                            }

                            @Override
                            public void visitFunctionDef(@NotNull TestFunctionDef o) {
                                if (o.getName() != null && o.getName().equals(name)) {
                                    found.set(true);
                                }
                            }

                            @Override
                            public void visitClassDef(@NotNull TestClassDef o) {
                                if (o.getName() != null && o.getName().equals(name)) {
                                    found.set(true);
                                    table.nodeTable.put(u,
                                            new PsiElementInfo(
                                                    u,
                                                    null,
                                                    Set.of(TestSyntaxHighlighter.CLASS_REF_NAME)
                                            ));
                                }
                            }

                            @Override
                            public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                                if (o.getName() != null && o.getName().equals(name)) {
                                    found.set(true);
                                }
                            }
                        });
                    }
                    else {
                        // trying to inherit a non-existing class
                        // the error has already been reported in the
                        // definition phase
                        break;
                    }
                }
            }

            if (symbol == null){
                if (!found.get()) {
                    table.nodeTable.put(u,
                            new PsiElementInfo(u,
                                    new ErrorMessage("can not find '" + name + "'", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_MARK)));
                }
            }
            else if (symbol.where == Scope.Kind.CLASS && (symbol.kind == Symbol.Kind.VARIABLE || symbol.kind == Symbol.Kind.CONSTANT)){
                table.nodeTable.put(u,
                        new PsiElementInfo(
                                u,
                                null,
                                Set.of(TestSyntaxHighlighter.MEMBER_REF_NAME)
                        ));
            }
            else if (symbol.where == Scope.Kind.GLOBAL && BUILT_IN_TYPES.contains(name)){
                table.nodeTable.put(u,
                        new PsiElementInfo(
                                u,
                                null,
                                Set.of(TestSyntaxHighlighter.CLASS_REF_NAME)
                        ));
            }
            else if (symbol.where == Scope.Kind.GLOBAL && BUILT_IN_FUNCTIONS.contains(name)){
                table.nodeTable.put(u,
                        new PsiElementInfo(
                                u,
                                null,
                                Set.of(TestSyntaxHighlighter.BUILTIN_REF_NAME)
                        ));
            }

        }

        @Override
        public void visitChainableIdentifier(@NotNull TestChainableIdentifier o) {
            o.getIdentifierList().get(0).accept(this);
        }

        @Override
        public void visitSuperAccess(@NotNull TestSuperAccess o) {
            if (inCall(o)) return;

            table.nodeTable.put(o.getNameIdentifier(),
                   new PsiElementInfo(
                           o.getNameIdentifier(),
                           null,
                           Set.of(TestSyntaxHighlighter.MEMBER_REF_NAME)
                   ));
        }


        @Override
        public void visitMemAccess(@NotNull TestMemAccess o) {
            if (inThisAccess(o)){

                Function<Scope, ContinueAction> searchInThisClass = scope -> {
                    if (scope.kind == Scope.Kind.GLOBAL) return ContinueAction.SUCCESS;
                    if (scope.kind == Scope.Kind.CLASS){
                        ContinueAction[] action = new ContinueAction[]{ContinueAction.STOP};
                        scope.psiElement.acceptChildren(new TestVisitor(){
                            @Override
                            public void visitElement(@NotNull PsiElement element) {
                                element.acceptChildren(this);
                            }

                            @Override
                            public void visitPsiElement(@NotNull PsiElement o) {
                                o.acceptChildren(this);
                            }

                            @Override
                            public void visitFunctionDef(@NotNull TestFunctionDef u) {
                                if (Objects.equals(o.getName(), u.getName())){
                                    action[0] = ContinueAction.SUCCESS;
                                }
                            }

                            @Override
                            public void visitSingleVar(@NotNull TestSingleVar u) {
                                if (Objects.equals(o.getName(), u.getName())){
                                    action[0] = ContinueAction.SUCCESS;
                                }
                            }

                            @Override
                            public void visitSingleConst(@NotNull TestSingleConst u) {
                                if (Objects.equals(o.getName(), u.getName())){
                                    action[0] = ContinueAction.SUCCESS;
                                }
                            }

                            @Override
                            public void visitClassDef(@NotNull TestClassDef u) {
                                if (Objects.equals(o.getName(), u.getName())){
                                    action[0] = ContinueAction.SUCCESS;
                                }
                            }
                        });
                        return action[0];
                    }
                    return ContinueAction.RESUME;
                };

                if (!table.checkHierarchy(searchInThisClass)){
                    table.nodeTable.put(o.getNameIdentifier(),
                            new PsiElementInfo(
                                    o.getNameIdentifier(),
                                    new ErrorMessage("can not find '" + o.getName() + "'", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_MARK)
                            ));
                    return;
                }
            }

            if (inCall(o)) return;
            table.nodeTable.put(o.getNameIdentifier(),
                    new PsiElementInfo(
                            o.getNameIdentifier(),
                            null,
                            Set.of(TestSyntaxHighlighter.MEMBER_REF_NAME)
                    ));
        }

        private boolean inCall(PsiElement element){
            return element.getParent() instanceof TestUnaryExpr u && !u.getCallList().isEmpty();
        }

        private boolean inThisAccess(PsiElement element){
            return element.getParent() instanceof TestUnaryExpr u && u.getExpr() instanceof TestThisExpr;
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();
        }

        @Override
        public void visitConstructorDef(@NotNull TestConstructorDef o) {
            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();
        }
    }


    private static class DependencyChecker extends TestVisitor {

        private final Table table;
        private final Hierarchy hierarchy;
        private final LinkedList<String> accessDepth = new LinkedList<>();

        // maps A -> B if A extends B
        private final Map<Symbol, Symbol> inheritanceMap = new HashMap<>();

        public DependencyChecker(Table table, Hierarchy hierarchy) {
            this.table = table;
            this.hierarchy = hierarchy;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitPsiElement(@NotNull PsiElement o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            file.acceptChildren(this);

            Set<Symbol> left = new HashSet<>(inheritanceMap.keySet());
            for (Symbol s : left.toArray(new Symbol[0])){
                checkInfiniteInheritances(s, left, new HashSet<>());
            }
        }

        private void checkInfiniteInheritances(Symbol source, Set<Symbol> left, Set<Symbol> visited){
            Symbol superSym = inheritanceMap.get(source);

            if (visited.contains(superSym)){

                Symbol curr = source;

                do {
                    table.nodeTable.put(curr.element,
                            new PsiElementInfo(
                                    curr.element,
                                    new ErrorMessage("infinite inheritance cycle", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));

                    curr = inheritanceMap.get(curr);
                }
                while (curr != source);

            }

            visited.add(source);
            left.remove(source);

            if (left.isEmpty()) return;
            checkInfiniteInheritances(superSym, left, visited);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            TestChainableIdentifier superIdent = o.getSuper();
            accessDepth.addLast(o.getName());

            if (superIdent != null){
                if (BUILT_IN_TYPES.contains(superIdent.getIdentifierList().get(0).getName())){
                    table.nodeTable.put(superIdent.getIdentifierList().get(0),
                            new PsiElementInfo(
                                    superIdent.getIdentifierList().get(0),
                                    new WarningMessage(o.getName() + " inherits a builtin-type", null),
                                    Set.of(TestSyntaxHighlighter.WARNING_UNDERLINE)
                            ));
                }

                List<String> list = new ArrayList<>();
                for (TestIdentifier ident : superIdent.getIdentifierList()){
                    list.add(ident.getName());
                }

                Symbol sym = hierarchy.search(list.subList(0, 1));

                if (sym != null) {

                    sym = hierarchy.search(list);

                    if (sym == null) {
                        int failIndex = hierarchy.getFailIndex(list);
                        TestIdentifier ident = superIdent.getIdentifierList().get(failIndex);

                        StringBuilder leastValid = new StringBuilder();
                        Iterator<String> iter = list.iterator();
                        leastValid.append(iter.next());

                        for (int i = 1; i < failIndex; i++) {
                            leastValid.append(".").append(iter.next());
                        }

                        table.nodeTable.put(ident,
                                new PsiElementInfo(
                                        ident,
                                        new ErrorMessage(leastValid + " has no class member " + list.get(failIndex), null),
                                        Set.of(TestSyntaxHighlighter.ERROR_MARK)
                                ));
                    }
                    else if (sym.kind != Symbol.Kind.CLASS){
                        StringBuilder fullName = new StringBuilder();
                        Iterator<String> iter = list.iterator();
                        fullName.append(iter.next());

                        for (int i = 1; i < list.size(); i++) {
                            fullName.append(".").append(iter.next());
                        }

                        table.nodeTable.put(superIdent,
                                new PsiElementInfo(
                                        superIdent,
                                        new ErrorMessage(fullName + " is not a class", null),
                                        Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                                ));
                    }
                    else {
                        Symbol thisSym = hierarchy.search(accessDepth);
                        inheritanceMap.put(sym, thisSym);
                    }
                }
            }

            o.acceptChildren(this);
            accessDepth.removeLast();
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            accessDepth.addLast(o.getName());
            o.acceptChildren(this);
            accessDepth.removeLast();
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            String name = o.getName();
            assert name != null;
            Symbol sym = hierarchy.search(List.of(name));
            if (sym != null && sym.kind == Symbol.Kind.CLASS){
                TestClassDef classDef = (TestClassDef) sym.element.getParent();
                if (classDef.getAbstractElement() != null && inCall(o)){
                    table.nodeTable.put(o.getParent(),
                            new PsiElementInfo(
                                    o.getParent(),
                                    new ErrorMessage("can not instantiate abstract class", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));
                }
            }
        }

        @Override
        public void visitConstructorDef(@NotNull TestConstructorDef o) {
            Set<String> paramList = new HashSet<>();
            for (TestParam param : o.getParamList()){
                paramList.add(param.getName());
            }

            TestCall superCall = o.getCall();
            if (superCall != null){
                TestArgList arguments = superCall.getArgList();
                if (arguments != null){
                    List<TestArg> args = arguments.getArgList();
                    for (TestArg arg : args){
                        arg.accept(new TestVisitor(){
                            @Override
                            public void visitPsiElement(@NotNull PsiElement o) {
                                o.acceptChildren(this);
                            }

                            @Override
                            public void visitElement(@NotNull PsiElement element) {
                                element.acceptChildren(this);
                            }

                            @Override
                            public void visitThisExpr(@NotNull TestThisExpr o) {
                                table.nodeTable.put(arg.getExpr().getParent(),
                                        new PsiElementInfo(
                                                arg.getExpr().getParent(),
                                                new ErrorMessage("Cannot reference 'this' before supertype constructor has been called", null),
                                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                                        ));
                            }

                            @Override
                            public void visitSuperAccess(@NotNull TestSuperAccess o) {
                                table.nodeTable.put(arg.getExpr().getParent(),
                                        new PsiElementInfo(
                                                arg.getExpr().getParent(),
                                                new ErrorMessage("Cannot reference '" + Objects.requireNonNull(arg.getExpr().getSuperAccess()).getName() + "' before supertype constructor has been called", null),
                                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                                        ));
                                o.acceptChildren(this);
                            }

                            @Override
                            public void visitIdentifier(@NotNull TestIdentifier o) {
                                Symbol globalSym = table.root.table.get(o.getName());
                                if (globalSym != null){
                                    if (!paramList.contains(o.getName()) && (globalSym.kind == Symbol.Kind.VARIABLE || globalSym.kind == Symbol.Kind.CONSTANT)) {
                                        table.nodeTable.put(o,
                                                new PsiElementInfo(
                                                        o,
                                                        new WeakWarningMessage(o.getName() + " may not have been initialized", null),
                                                        Set.of(TestSyntaxHighlighter.WEAK_WARNING_UNDERLINE)
                                                ));
                                    }
                                }
                                else if (!paramList.contains(o.getName())){
                                    PsiElementInfo info = table.nodeTable.get(o);
                                    if (info == null || !(info.message instanceof ErrorMessage)){
                                        table.nodeTable.put(o,
                                                new PsiElementInfo(
                                                        o,
                                                        new ErrorMessage("Cannot reference '" + o.getName() + "' before supertype constructor has been called", null),
                                                        Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                                                ));
                                    }
                                }
                            }
                        });
                    }
                }
            }

            if (o.getBlock() != null)
                o.getBlock().accept(this);
        }

        private boolean inCall(PsiElement element){
            return element.getParent() instanceof TestUnaryExpr u && !u.getCallList().isEmpty();
        }
    }

    private static class ScopeChecker extends TestVisitor {
        private final Table table;
        private boolean inClass = false;
        private boolean inLoop = false;
        private boolean inFunction = false;
        private boolean inLambda = false;

        private ScopeChecker(Table table) {
            this.table = table;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitPsiElement(@NotNull PsiElement o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            boolean inClassTemp = this.inClass;
            this.inClass = true;
            o.acceptChildren(this);
            this.inClass = inClassTemp;
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            boolean inFunctionTemp = this.inFunction;
            this.inFunction = true;
            o.acceptChildren(this);
            this.inFunction = inFunctionTemp;
        }

        @Override
        public void visitThisExpr(@NotNull TestThisExpr o) {
            if (!inClass && !inLambda){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot use 'this' out of class", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        @Override
        public void visitSuperAccess(@NotNull TestSuperAccess o) {
            if (!inClass){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot use 'super' out of class", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        @Override
        public void visitWhileDo(@NotNull TestWhileDo o) {
            boolean inLoopTemp = this.inLoop;
            this.inLoop = true;
            o.acceptChildren(this);
            this.inLoop = inLoopTemp;
        }

        @Override
        public void visitDoWhile(@NotNull TestDoWhile o) {
            boolean inLoopTemp = this.inLoop;
            this.inLoop = true;
            o.acceptChildren(this);
            this.inLoop = inLoopTemp;
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            boolean inLoopTemp = this.inLoop;
            this.inLoop = true;
            o.acceptChildren(this);
            this.inLoop = inLoopTemp;
        }

        @Override
        public void visitBreakStmt(@NotNull TestBreakStmt o) {
            if (!inLoop){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot use 'break' out of loop", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        @Override
        public void visitContinueStmt(@NotNull TestContinueStmt o) {
            if (!inLoop){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot use 'continue' out of loop", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        @Override
        public void visitReturnStmt(@NotNull TestReturnStmt o) {
            if (!inFunction && !inLambda){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot use 'return' out of function", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            boolean inLambdaTemp = this.inLambda;
            this.inLambda = true;
            boolean inClassTemp = this.inClass;
            this.inClass = false;
            boolean inFunctionTemp = this.inFunction;
            this.inFunction = false;
            boolean inLoopTemp = this.inLoop;
            this.inLoop = false;
            o.acceptChildren(this);
            this.inLambda = inLambdaTemp;
            this.inClass = inClassTemp;
            this.inFunction = inFunctionTemp;
            this.inLoop = inLoopTemp;
        }

    }

    private static class TypeChecker extends TestVisitor {
        @Override
        public void visitPsiElement(@NotNull PsiElement o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitPlusOp(@NotNull TestPlusOp o) {
            super.visitPlusOp(o);
        }
    }

    private static class FlowAnalyzer extends TestVisitor {

        private final Table table;

        private FlowAnalyzer(Table table) {
            this.table = table;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitPsiElement(@NotNull PsiElement o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitBlock(@NotNull TestBlock o) {
            PsiElement[] children = o.getChildren();
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof TestReturnStmt || children[i] instanceof TestThrowStmt){
                    if (i + 1 < children.length){
                        table.nodeTable.putIfAbsent(children[i + 1],
                                new PsiElementInfo(
                                        children[i + 1],
                                        new WarningMessage("unreachable statement", null),
                                        Set.of(TestSyntaxHighlighter.WARNING_UNDERLINE)
                                ));
                    }
                }
            }
        }
    }

    public enum ContinueAction {
        RESUME,
        STOP,
        SUCCESS
    }

    private static class Table {
        public final Scope root;
        public Scope currentScope;
        public final Map<PsiElement, PsiElementInfo> nodeTable;
        private Table(Scope root, Map<PsiElement, PsiElementInfo> nodeTable) {
            this.root = root;
            currentScope = root;
            this.nodeTable = nodeTable;
        }
        public void enterScope(PsiElement element){
            currentScope = currentScope.children.get(element);
            if (currentScope == null)
                throw new AssertionError();
        }
        public void leaveScope(){
            currentScope = currentScope.parent;
        }
        public void moveTopLevel(){
            currentScope = root;
        }

        public boolean checkHierarchy(Function<Scope, ContinueAction> tester){
            Scope curr = currentScope;
            while (curr != null){
                ContinueAction action = tester.apply(curr);
                if (action == ContinueAction.SUCCESS){
                    return true;
                }
                if (action == ContinueAction.STOP)
                    return false;
                curr = curr.parent;
            }
            return false;
        }

        public Symbol search(Function<Scope, Symbol> tester){
            Scope scope = currentScope;
             do {
                 Symbol sym = scope.search(tester);
                 if (sym != null) return sym;
                 scope = scope.parent;
             }while (scope != null);
             return null;
        }
    }


    private static class Scope {

        private Scope(Kind kind, Scope parent, PsiElement element) {
            this.kind = kind;
            this.parent = parent;
            this.psiElement = element;
        }

        private Scope(Kind kind, PsiElement element) {
            this(kind, null, element);
        }

        public enum Kind {
            BLOCK,
            GLOBAL,
            FUNCTION,
            CONSTRUCTOR,
            CLASS,
            NAMESPACE
        }

        public final Kind kind;
        public final Scope parent;
        public final PsiElement psiElement;
        public final Map<String, Symbol> table = new HashMap<>();
        public Map<PsiElement, Scope> children = new HashMap<>();

        public Symbol search(Function<Scope, Symbol> tester){
            return tester.apply(this);
        }
    }

    private static class Symbol {

        private Symbol(String name, PsiElement element, Symbol.Kind kind, Scope.Kind where) {
            this.name = name;
            this.element = element;
            this.kind = kind;
            this.where = where;
        }

        public enum Kind {
            VARIABLE,
            CONSTANT,
            FUNCTION,
            CLASS,
            NAMESPACE,
            UNKNOWN
        }

        public final String name;
        public final PsiElement element;
        public final Symbol.Kind kind;
        public final Scope.Kind where;

    }


    private static class Hierarchy {
        public final Layer topLevel;
        private Hierarchy(Layer topLevel) {
            this.topLevel = topLevel;
        }

        public Symbol search(List<String> list) {
            return topLevel.search(list.iterator());
        }

        public int getFailIndex(List<String> list) {
            return topLevel.getFailIndex(list.iterator(), 0);
        }

        public static class Layer {
            final Symbol symbol;
            Map<String, Layer> subLayers = new HashMap<>();
            public Layer(Symbol symbol) {
                this.symbol = symbol;
            }
            public Symbol search(Iterator<String> iterator) {
                if (!iterator.hasNext()) return symbol;
                String key = iterator.next();
                Layer layer = subLayers.get(key);
                if (layer == null) return null;
                return layer.search(iterator);
            }
            public int getFailIndex(Iterator<String> iterator, int index) {
                if (!iterator.hasNext()) return index;
                String key = iterator.next();
                Layer layer = subLayers.get(key);
                if (layer == null) return index;
                return layer.getFailIndex(iterator, index+1);
            }
        }
        public Symbol get(String name) {
            return null;
        }
    }



    private static class PsiElementInfo {
        public final PsiElement element;
        final Set<TextAttributesKey> attributes = new HashSet<>();
        public final HoverMessage message;

        private PsiElementInfo(PsiElement element, HoverMessage message, Collection<TextAttributesKey> attributes) {
            this.element = element;
            this.message = message;
            this.attributes.addAll(attributes);
        }
    }

    private static class Fix {
        final LocalQuickFix baseFix;
        String description;
        LocalQuickFix[] additionalFixes;

        private Fix(LocalQuickFix baseFix, String description, LocalQuickFix... additionalFixes) {
            this.baseFix = baseFix;
            this.description = description;
            this.additionalFixes = additionalFixes;
        }
    }

    private static abstract class HoverMessage {
        final String message;
        final Fix quickFix;

        public HoverMessage(String message, Fix quickFix) {
            this.message = message;
            this.quickFix = quickFix;
        }

        abstract void apply(PsiElementInfo info, AnnotationHolder holder);
    }

    private static class ErrorMessage extends HoverMessage {

        public ErrorMessage(String message, Fix quickFix) {
            super(message, quickFix);
        }

        @Override
        void apply(PsiElementInfo info, AnnotationHolder holder) {
            AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
                    .highlightType(ProblemHighlightType.ERROR)
                    .range(info.element);

            TextAttributesKey[] keys = info.attributes.toArray(new TextAttributesKey[0]);
            builder = builder.textAttributes(Styles.mergeAttributes(keys));

            if (quickFix != null) {
                builder = builder.newLocalQuickFix(quickFix.baseFix, makeDescriptor(info.element, quickFix.description, quickFix.additionalFixes)).registerFix();
            }

            builder.create();
        }
    }

    private static class WarningMessage extends HoverMessage {

        public WarningMessage(String message, Fix quickFix) {
            super(message, quickFix);
        }

        @Override
        void apply(PsiElementInfo info, AnnotationHolder holder) {
            AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.WARNING, message)
                    .highlightType(ProblemHighlightType.WARNING)
                    .range(info.element);

            TextAttributesKey[] keys = info.attributes.toArray(new TextAttributesKey[0]);
            builder = builder.textAttributes(Styles.mergeAttributes(keys));

            if (quickFix != null) {
                builder = builder.newLocalQuickFix(quickFix.baseFix, makeDescriptor(info.element, quickFix.description, quickFix.additionalFixes)).registerFix();
            }

            builder.create();
        }
    }

    private static class WeakWarningMessage extends HoverMessage {

        public WeakWarningMessage(String message, Fix quickFix) {
            super(message, quickFix);
        }

        @Override
        void apply(PsiElementInfo info, AnnotationHolder holder) {
            AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
                    .highlightType(ProblemHighlightType.WARNING)
                    .range(info.element);

            TextAttributesKey[] keys = info.attributes.toArray(new TextAttributesKey[0]);
            builder = builder.textAttributes(Styles.mergeAttributes(keys));

            if (quickFix != null) {
                builder = builder.newLocalQuickFix(quickFix.baseFix, makeDescriptor(info.element, quickFix.description, quickFix.additionalFixes)).registerFix();
            }

            builder.create();
        }
    }


    private static class LinkToDuplicateFix implements LocalQuickFix {

        private final PsiElement element;

        private LinkToDuplicateFix(PsiElement element) {
            this.element = element;
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return "Duplicate definition";
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            if (element instanceof Navigatable n)
                n.navigate(true);
        }
    }


    private static ProblemDescriptor makeDescriptor(PsiElement element, String description, LocalQuickFix... additionalFixes){
        return new ProblemDescriptorBase(element, element, description,  additionalFixes, ProblemHighlightType.ERROR, false, element.getTextRange(), true, true);
    }
}

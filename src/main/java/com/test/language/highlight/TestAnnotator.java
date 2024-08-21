package com.test.language.highlight;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.SmartPsiElementPointer;
import com.test.language.psi.*;
import com.test.language.run.build.BuildTscriptTask;
import com.test.settings.WebSelectAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

final class TestAnnotator implements Annotator {

    private static final Set<String> BUILT_IN_FUNCTIONS = Set.of("print", "exit", "error", "assert");
    private static final Set<String> BUILT_IN_TYPES =
            Set.of("Integer", "Real", "Boolean", "String", "Null", "Array", "Dictionary", "Range", "Function", "Type");

    private static boolean targetWebTscript;

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

        if (element instanceof PsiFile file){
            PropertiesComponent properties = PropertiesComponent.getInstance(file.getProject());
            targetWebTscript = properties.getBoolean(WebSelectAction.KEY);
            performAction(file, holder);
        }

    }


    private void performAction(PsiFile file, AnnotationHolder holder){

        // notify file modification
        BuildTscriptTask.cached.remove(file.getVirtualFile().getPath());

        SymbolResolver symbolResolver = new SymbolResolver();
        file.accept(symbolResolver);
        Table table = new Table(symbolResolver.scope, symbolResolver.nodeTable);

        HierarchyResolver hierarchyResolver = new HierarchyResolver(table);
        file.accept(hierarchyResolver);
        Hierarchy hierarchy = hierarchyResolver.hierarchy;

        file.accept(new DefinitionChecker(table, hierarchy));
        file.accept(new DependencyChecker(table, hierarchy));
        file.accept(new ScopeChecker(table));
        
        TypeResolver typeResolver = new TypeResolver();
        file.accept(typeResolver);
        Map<String, Type> typeTable = typeResolver.typeTable;
        file.accept(new TypeChecker(table, typeTable));
        
        
        file.accept(new FlowAnalyzer(table));

        for (PsiElement e : table.nodeTable.keySet()){
            PsiElementInfo info = table.nodeTable.get(e);
            if (info.message != null && info.element != null){
                AnnotationBuilder builder = info.message.createAnnotationBuilder(info, holder);

                TextAttributesKey[] keys = info.attributes.toArray(new TextAttributesKey[0]);
                builder = builder.textAttributes(Styles.mergeAttributes(keys));

                Fix quickFix = info.message.quickFix;
                if (quickFix != null && quickFix.baseFix != null) {
                    builder = builder.newLocalQuickFix(quickFix.baseFix, makeDescriptor(info.element, quickFix.description, quickFix.additionalFixes)).registerFix();
                }

                builder.create();
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
        private Visibility currentVisibility;
        public Map<PsiElement, PsiElementInfo> nodeTable = new HashMap<>();

        @Override
        public void visitFile(@NotNull PsiFile file) {
            scope = new Scope(Scope.Kind.GLOBAL, file);

            // LOAD BUILT-INS
            BUILT_IN_FUNCTIONS.forEach(func -> putIfAbsent(scope, func, null, file, Symbol.Kind.FUNCTION, null));
            BUILT_IN_TYPES.forEach(func -> putIfAbsent(scope, func, null, file, Symbol.Kind.CLASS, null));

            file.acceptChildren(this);
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (o.getAbstractElement() != null) {
                nodeTable.put(o.getAbstractElement(),
                        new PsiElementInfo(
                                o.getAbstractElement(),
                                new ErrorMessage("web tscript does not support keyword 'const'", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
            putIfAbsent(scope, o.getName(), currentVisibility, o.getNameIdentifier(), Symbol.Kind.CLASS, TestSyntaxHighlighter.CLASS_DEF_NAME);
            Scope previous = scope;
            scope = new Scope(Scope.Kind.CLASS, scope, o);
            previous.children.put(o, scope);
            Visibility visTmp = currentVisibility;
            currentVisibility = null;
            o.acceptChildren(this);
            currentVisibility = visTmp;
            scope = previous;
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            putIfAbsent(scope, o.getName(), currentVisibility, o.getNameIdentifier(), Symbol.Kind.NAMESPACE, null);
            Scope previous = scope;
            scope = new Scope(Scope.Kind.NAMESPACE, scope, o);
            previous.children.put(o, scope);
            Visibility visTmp = currentVisibility;
            currentVisibility = null;
            o.acceptChildren(this);
            currentVisibility = visTmp;
            scope = previous;
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (targetWebTscript){
                if (o.getAbstractElement() != null) {
                    nodeTable.put(o.getAbstractElement(),
                            new PsiElementInfo(
                                    o.getAbstractElement(),
                                    new ErrorMessage("web tscript does not support keyword 'const'", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));
                }
                if (o.getNativeElement() != null) {
                    nodeTable.put(o.getNativeElement(),
                            new PsiElementInfo(
                                    o.getNativeElement(),
                                    new ErrorMessage("web tscript does not support keyword 'native'", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));
                }
                if (o.getOverriddenElement() != null) {
                    nodeTable.put(o.getOverriddenElement(),
                            new PsiElementInfo(
                                    o.getOverriddenElement(),
                                    new ErrorMessage("web tscript does not support keyword 'overridden'", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));
                }
            }
            putIfAbsent(scope, o.getName(), currentVisibility, o.getNameIdentifier(), Symbol.Kind.FUNCTION, TestSyntaxHighlighter.FUNC_DEF_NAME);
            Scope previous = scope;
            scope = new Scope(Scope.Kind.FUNCTION, scope, o);
            previous.children.put(o, scope);
            Visibility visTmp = currentVisibility;
            currentVisibility = null;
            o.acceptChildren(this);
            currentVisibility = visTmp;
            scope = previous;
        }

        @Override
        public void visitConstructorDef(@NotNull TestConstructorDef o) {
            Scope previous = scope;
            scope = new Scope(Scope.Kind.CONSTRUCTOR, scope, o);
            previous.children.put(o, scope);
            Visibility visTmp = currentVisibility;
            currentVisibility = null;
            o.acceptChildren(this);
            currentVisibility = visTmp;
            scope = previous;
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            Scope previous = scope;
            scope = new Scope(Scope.Kind.BLOCK, scope, o);
            previous.children.put(o, scope);

            if (o.getName() != null){
                putIfAbsent(scope, o.getName(), null, o.getNameIdentifier(), Symbol.Kind.VARIABLE, null);
            }

            o.acceptChildren(this);
            scope = previous;
        }

        @Override
        public void visitTryCatch(@NotNull TestTryCatch o) {
            if (!o.getStmtList().isEmpty()) {
                o.getStmtList().get(0).accept(this);
            }

            if (o.getStmtList().size() == 2) {
                Scope previous = scope;
                scope = new Scope(Scope.Kind.BLOCK, scope, o);
                previous.children.put(o, scope);
                if (o.getName() != null)
                    putIfAbsent(scope, o.getName(), null, o.getNameIdentifier(), Symbol.Kind.VARIABLE, null);
                o.getStmtList().get(1).accept(this);
                scope = previous;
            }
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
                putIfAbsent(scope, s.getName(), currentVisibility, s.getNameIdentifier(), Symbol.Kind.VARIABLE, key);
                if (s.getExpr() != null)
                    s.getExpr().accept(this);
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            if (targetWebTscript){
                nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("web tscript does not support keyword 'const'", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
            TextAttributesKey key = null;
            if (scope.kind == Scope.Kind.CLASS) key = TestSyntaxHighlighter.MEMBER_REF_NAME;
            for (TestSingleConst s : o.getSingleConstList()) {
                putIfAbsent(scope, s.getName(), currentVisibility, s.getNameIdentifier(), Symbol.Kind.CONSTANT, key);
                if (s.getExpr() != null)
                    s.getExpr().accept(this);
            }

        }

        @Override
        public void visitParam(@NotNull TestParam o) {
            putIfAbsent(scope, o.getName(),null, o.getNameIdentifier(), Symbol.Kind.VARIABLE, null);
        }

        @Override
        public void visitVisibility(@NotNull TestVisibility o) {
            if (o.getName() == null){
                currentVisibility = null;
                return;
            }

            currentVisibility = switch (o.getName().toLowerCase()){
                case "public" -> Visibility.PUBLIC;
                case "private" -> Visibility.PRIVATE;
                case "protected" -> Visibility.PROTECTED;
                default -> throw new AssertionError();
            };
        }

        private void putIfAbsent(Scope scope, String name, Visibility visibility, PsiElement element, Symbol.Kind kind, TextAttributesKey extraKey){
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

            scope.table.put(name, new Symbol(name, visibility, element, kind, scope.kind));
            if (!keys.isEmpty())
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
        private String currentDefined = null;

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
        public void visitBlock(@NotNull TestBlock o) {
            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();
        }

        @Override
        public void visitTryCatch(@NotNull TestTryCatch o) {
            if (o.getStmtList().isEmpty()) return;

            o.getStmtList().get(0).accept(this);

            if (o.getStmtList().size() == 2){
                table.enterScope(o);
                o.getStmtList().get(1).accept(this);
                table.leaveScope();
            }
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
        public void visitSingleVar(@NotNull TestSingleVar o) {
            currentDefined = o.getName();
            o.acceptChildren(this);
            currentDefined = null;
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier u) {
            String name = u.getName();
            Symbol symbol = table.search(s -> s.table.get(name));

            final Member[] superMember = {null};
            Symbol.Kind[] superMemberKind = {null};

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
                        if (def.getClassBodyDef() != null)
                            def.getClassBodyDef().acceptChildren(new TestVisitor() {
                                Visibility visibility;
                                @Override
                                public void visitElement(@NotNull PsiElement element) {
                                    element.acceptChildren(this);
                                }

                                @Override
                                public void visitPsiElement(@NotNull PsiElement o) {
                                    o.acceptChildren(this);
                                }

                                @Override
                                public void visitVisibility(@NotNull TestVisibility o) {
                                    if (o.getName() == null) return;
                                    visibility = switch (o.getName().toLowerCase()){
                                        case "public" -> Visibility.PUBLIC;
                                        case "private" -> Visibility.PRIVATE;
                                        case "protected" -> Visibility.PROTECTED;
                                        default -> throw new AssertionError();
                                    };
                                }

                                @Override
                                public void visitSingleVar(@NotNull TestSingleVar o) {
                                    if (o.getName() != null && o.getName().equals(name)) {
                                        superMember[0] = new Member(o.getName(), visibility,"var", false);
                                        superMemberKind[0] = Symbol.Kind.VARIABLE;
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
                                        superMember[0] = new Member(o.getName(), visibility,"var", false);
                                        superMemberKind[0] = Symbol.Kind.VARIABLE;
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
                                        superMemberKind[0] = Symbol.Kind.FUNCTION;
                                        superMember[0] = new Member(o.getName(), visibility,"", false);
                                    }
                                }

                                @Override
                                public void visitClassDef(@NotNull TestClassDef o) {
                                    if (o.getName() != null && o.getName().equals(name)) {
                                        superMemberKind[0] = Symbol.Kind.CLASS;
                                        superMember[0] = new Member(o.getName(), visibility,"", false);
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
                                        superMemberKind[0] = Symbol.Kind.NAMESPACE;
                                        superMember[0] = new Member(o.getName(), visibility,"", false);
                                    }
                                }

                                @Override
                                public void visitForLoop(@NotNull TestForLoop o) {
                                    visitElement(o);
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
                // symbol is not found in current class
                if (superMember[0] != null){
                    // found in super class
                    Set<TextAttributesKey> styles = new HashSet<>(Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE));
                    if (superMemberKind[0] == Symbol.Kind.VARIABLE)
                        styles.add(TestSyntaxHighlighter.MEMBER_REF_NAME);

                    if (superMember[0].visibility == Visibility.PRIVATE) {
                        table.nodeTable.put(u,
                                new PsiElementInfo(u,
                                        new ErrorMessage(superMember[0].name + " has private access", null),
                                        styles));
                    }
                }
                else {
                    // not found at all
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
            else if (currentDefined != null && currentDefined.equals(u.getName())){
                table.nodeTable.put(u,
                        new PsiElementInfo(
                                u,
                                new ErrorMessage("can not use " + currentDefined + " before it is defined", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
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
                        TestClassDef def = (TestClassDef) scope.psiElement;
                        ContinueAction[] action = new ContinueAction[]{ContinueAction.STOP};
                        if (def.getClassBodyDef() != null)
                            def.getClassBodyDef().acceptChildren(new TestVisitor(){
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
            if (o.getNativeElement() != null && o.getBlock() != null){
                table.nodeTable.put(o.getNativeElement(),
                        new PsiElementInfo(
                                o.getNativeElement(),
                                new ErrorMessage("native function must not have a body", new Fix(new RemoveTextFix("make function not native", o.getNativeElement()), "")),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
            else if (o.getAbstractElement() != null && o.getBlock() != null){
                table.nodeTable.put(o.getAbstractElement(),
                        new PsiElementInfo(
                                o.getAbstractElement(),
                                new ErrorMessage("abstract function must not have a body", new Fix(new RemoveTextFix("make function not abstract", o.getAbstractElement()), "")),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
            else if (o.getBlock() == null && o.getNativeElement() == null && o.getAbstractElement() == null){
                table.nodeTable.put(o.getNameIdentifier(),
                        new PsiElementInfo(
                                o.getNameIdentifier(),
                                new ErrorMessage("missing function body", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE, TestSyntaxHighlighter.FUNC_DEF_NAME)
                        ));
            }

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
            if (!targetWebTscript) return;
            String name = o.getName();
            assert name != null;
            Symbol sym = hierarchy.search(List.of(name));
            if (sym != null && sym.kind == Symbol.Kind.CLASS){
                TestClassDef classDef = (TestClassDef) sym.element.getParent();
                if (classDef.getAbstractElement() != null && inCall(o)){
                    table.nodeTable.put(o.getParent(),
                            new PsiElementInfo(
                                    o.getParent(),
                                    new ErrorMessage("can not instantiate abstract class", new Fix(new RemoveTextFix("make '" + o.getName() + "' not abstract", classDef.getAbstractElement()), "")),
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
                                table.nodeTable.put(o.getNameIdentifier(),
                                        new PsiElementInfo(
                                                o.getNameIdentifier(),
                                                new ErrorMessage("Cannot reference '" + Objects.requireNonNull(o.getName())+ "' before supertype constructor has been called", null),
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
        private boolean inStaticFunction = false;

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

        private void checkStatic(PsiElement staticElement){
            if (staticElement != null && !inClass){
                table.nodeTable.put(staticElement,
                        new PsiElementInfo(
                                staticElement,
                                new ErrorMessage("Cannot use 'static' out of class", new Fix(new RemoveTextFix("remove keyword 'super'"), "test")),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            checkStatic(o.getStaticElement());
            boolean inClassTemp = this.inClass;
            this.inClass = true;
            o.acceptChildren(this);
            this.inClass = inClassTemp;
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            checkStatic(o.getStaticElement());
            boolean inClassTemp = this.inClass;
            this.inClass = false;
            o.acceptChildren(this);
            this.inClass = inClassTemp;
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            checkStatic(o.getStaticElement());
            boolean inFunctionTemp = this.inFunction;
            this.inFunction = true;
            boolean inStaticTemp = this.inStaticFunction;
            this.inStaticFunction = o.getStaticElement() != null;
            o.acceptChildren(this);
            this.inFunction = inFunctionTemp;
            this.inStaticFunction = inStaticTemp;
        }

        @Override
        public void visitThisExpr(@NotNull TestThisExpr o) {
            if (!inLambda) {
                if (!inClass) {
                    table.nodeTable.put(o,
                            new PsiElementInfo(
                                    o,
                                    new ErrorMessage("Cannot use 'this' out of class", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));
                } else if (inStaticFunction) {
                    table.nodeTable.put(o,
                            new PsiElementInfo(
                                    o,
                                    new ErrorMessage("Cannot use 'this' from a static context", null),
                                    Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                            ));
                }
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
            else if (inStaticFunction){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot use 'super' from a static context", null),
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
                                new ErrorMessage("Cannot use 'break' out of loop", new Fix(new RemoveTextFix("remove statement"), "")),
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
                                new ErrorMessage("Cannot use 'continue' out of loop", new Fix(new RemoveTextFix("remove statement"), "")),
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
                                new ErrorMessage("Cannot use 'return' out of function", new Fix(new RemoveTextFix("remove statement"), "")),
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
            boolean inStaticFunctionTemp = this.inStaticFunction;
            this.inStaticFunction = false;
            o.acceptChildren(this);
            this.inLambda = inLambdaTemp;
            this.inClass = inClassTemp;
            this.inFunction = inFunctionTemp;
            this.inLoop = inLoopTemp;
            this.inStaticFunction = inStaticFunctionTemp;
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

        private final Table table;
        private final Map<String, Type> typeTable;
        private final Deque<CheckScope> scopeStack = new ArrayDeque<>();

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
            CheckScope top = scopeStack.element();
            CheckScope newScope = new CheckScope(new ArrayDeque<>(), top.varTypes, new HashSet<>());
            scopeStack.push(newScope);
        }

        private void leaveBlockScope(){
            CheckScope top = scopeStack.pop();
            CheckScope newTop = scopeStack.element();
            for (String changed : top.changes){
                if (newTop.varTypes.containsKey(changed)){
                    newTop.varTypes.put(changed, UnknownType.INSTANCE);
                }
            }
        }

        private TypeChecker(Table table, Map<String, Type> typeTable) {
            this.table = table;
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
            scopeStack.push(new CheckScope());
            file.acceptChildren(this);
            scopeStack.pop();
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            scopeStack.push(new CheckScope());
            o.acceptChildren(this);
            scopeStack.pop();
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            scopeStack.push(new CheckScope());
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
                requireBoolean(o.getExpr());
            }

            enterBlockScope();
            if (o.getStmt() != null)
                o.getStmt().accept(this);
            leaveBlockScope();
        }

        @Override
        public void visitDoWhile(@NotNull TestDoWhile o) {
            enterBlockScope();
            if (o.getStmt() != null)
                o.getStmt().accept(this);

            if (o.getExpr() != null) {
                o.getExpr().accept(this);
                requireBoolean(o.getExpr());
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
                requireBoolean(o.getExpr());
            }

            if (o.getStmtList().size() == 1){
                // only if
                enterBlockScope();
                o.getStmtList().get(0).accept(this);
                leaveBlockScope();
            }
            else if (o.getStmtList().size() > 1){
                // if and else
                CheckScope top = scopeStack.element();

                CheckScope ifScope = new CheckScope(new ArrayDeque<>(), top.varTypes, new HashSet<>());
                scopeStack.push(ifScope);
                o.getStmtList().get(0).acceptChildren(this);
                scopeStack.pop();

                CheckScope elseScope = new CheckScope(new ArrayDeque<>(), top.varTypes, new HashSet<>());
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

        private void requireBoolean(PsiElement caller){
            if (!typeAvailable()) return;
            Type type = popType();
            if (type != UnknownType.INSTANCE && !type.getName().equals("Boolean")){
                table.nodeTable.put(caller,
                        new PsiElementInfo(
                                caller,
                                new ErrorMessage("type mismatch", new Fix(null, "required: Boolean\ngot: " + type.getName())),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        private void requireString(PsiElement caller){
            if (!typeAvailable()) return;
            Type type = popType();
            if (type != UnknownType.INSTANCE && !type.getName().equals("String")){
                table.nodeTable.put(caller,
                        new PsiElementInfo(
                                caller,
                                new ErrorMessage("type mismatch", new Fix(null, "required: String\ngot: " + type.getName())),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }

        private void requireInteger(PsiElement caller){
            if (!typeAvailable()) return;
            Type type = popType();
            if (type != UnknownType.INSTANCE && !type.getName().equals("Integer")){
                table.nodeTable.put(caller,
                        new PsiElementInfo(
                                caller,
                                new ErrorMessage("type mismatch", new Fix(null, "required: Integer\ngot: " + type.getName())),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }
        }


        private void requireIterable(PsiElement caller){
            if (!typeAvailable()) return;
            Type type = popType();
            if (!type.canItemAccess()){
                table.nodeTable.put(caller,
                        new PsiElementInfo(
                                caller,
                                new ErrorMessage("type mismatch", new Fix(null, type.getName() + " is not iterable")),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
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
            if (o.getExpr() == null){
                pushType(UnknownType.INSTANCE);
                return;
            }

            o.getExpr().accept(this);

            if (!typeAvailable()){
                pushType(UnknownType.INSTANCE);
                return;
            }

            Type type = popType();
            if (!type.canItemAccess()){
                table.nodeTable.put(o.getExpr(),
                        new PsiElementInfo(
                                o.getExpr(),
                                new ErrorMessage("type mismatch", new Fix(null, "can not access " + type.getPrintName())),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }

            if (type.getName().equals("String")){
                pushType(type);
            }
            else if (type.getName().equals("Range")){
                pushType(typeTable.get("Integer"));
            }
            else {
                pushType(UnknownType.INSTANCE);
            }
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
        public void visitDictionaryEntry(@NotNull TestDictionaryEntry o) {

            if (!o.getExprList().isEmpty()) {
                o.getExprList().get(0).accept(this);
                if (targetWebTscript) {
                    requireString(o.getExprList().get(0));
                }

                if (o.getExprList().size() == 2) {
                    o.getExprList().get(1).accept(this);
                    if (typeAvailable()) popType();
                }
            }

            pushType(typeTable.get("Dictionary"));
        }

        @Override
        public void visitRangeExpr(@NotNull TestRangeExpr o) {

            if (!o.getExprList().isEmpty()) {
                o.getExprList().get(0).accept(this);
                requireInteger(o.getExprList().get(0));

                if (o.getExprList().size() == 2) {
                    o.getExprList().get(1).accept(this);
                    requireInteger(o.getExprList().get(1));
                }
            }

            pushType(typeTable.get("Dictionary"));
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
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot invert " + type.getPrintName(), null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
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
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("Cannot negate " + type.getPrintName(), null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
                pushType(UnknownType.INSTANCE);
            }
            else {
                pushType(type);
            }
        }

        @Override
        public void visitTypeofPrefixExpr(@NotNull TestTypeofPrefixExpr o) {
            if (targetWebTscript){
                table.nodeTable.put(o,
                        new PsiElementInfo(
                                o,
                                new ErrorMessage("web tscript does not support unary typeof operator", null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
            }

            o.getExpr().accept(this);
            if (!typeAvailable()) return;
            Type top = popType();
            if (top == UnknownType.INSTANCE) {
                pushType(UnknownType.INSTANCE);
                return;
            }

            if (top instanceof WrappedType){
                pushType(typeTable.get("Type"));
            }
            else {
                pushType(new WrappedType(top));
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
                table.nodeTable.put(caller,
                        new PsiElementInfo(
                                caller,
                                new ErrorMessage("Cannot perform " + op.name + " on " + l.getPrintName() + " and " + r.getPrintName(), null),
                                Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                        ));
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
                                       BinaryOperationHandler handler){

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
                                       BinaryOperationHandler handler){

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
                        table.nodeTable.put(o,
                                new PsiElementInfo(
                                        o,
                                        new ErrorMessage("web tscript does not support shift operator", null),
                                        Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                                ));

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
                            table.nodeTable.put(o,
                                    new PsiElementInfo(
                                            o,
                                            new ErrorMessage("web tscript does not support binary typeof operator", null),
                                            Set.of(TestSyntaxHighlighter.ERROR_UNDERLINE)
                                    ));
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


    private enum Operation {
        ADD("addition"),
        SUB("subtraction"),
        MUL("multiplication"),
        DIV("division"),
        IDIV("integer division"),
        POW("exponentiation"),
        MOD("modulo operation"),
        SAL("left arithmetical shift"),
        SAR("right arithmetical shift"),
        SLR("right logical shift"),
        AND("and"),
        OR("or"),
        XOR("xor"),
        GT("'>'"),
        LT("'<'"),
        GEQ("'>='"),
        LEQ("'<='");

        public final String name;

        Operation(String name) {
            this.name = name;
        }
    }

    private enum Visibility {
        PUBLIC, PRIVATE, PROTECTED
    }

    private record Member(String name, Visibility visibility, Function<Map<String, Type>, Type> type, boolean isStatic){

        public Member(String name, Visibility visibility, String type, boolean isStatic){
            this(name, visibility, m -> m.get(type), isStatic);
        }

    }

    private interface Type {
        String getName();
        default String getPrintName(){ return getName(); }
        default Type operate(Operation op, Type type, Map<String, Type> typeTable){
            if (type == UnknownType.INSTANCE || !typeTable.containsKey(type.getName()))
                return UnknownType.INSTANCE;
            return performOperation(op, type, typeTable);
        }
        Type performOperation(Operation op, Type type, Map<String, Type> typeTable);
        Member getMember(String name, Map<String, Type> typeTable);
        boolean canItemAccess();
        boolean isCallable();
    }

    private static class WrappedType implements Type {
        private final Type type;
        private WrappedType(Type type) {
            this.type = type;
        }
        @Override
        public String getName() {
            return "Type<" + type.getName() + ">";
        }
        @Override
        public Type performOperation(Operation op, Type type, Map<String, Type> typeTable) {
            return null;
        }
        @Override
        public Member getMember(String name, Map<String, Type> typeTable) {
            Member member = type.getMember(name, typeTable);

            if (member != null && member.isStatic){
                return member;
            }

            return null;
        }
        @Override
        public boolean canItemAccess() {
            return false;
        }
        @Override
        public boolean isCallable() {
            return true;
        }
    }

    private static class UnknownType implements Type {
        public static Type INSTANCE = new UnknownType();
        private UnknownType(){}
        @Override public String getName() { return "unknown type"; }
        @Override public Type performOperation(Operation op, Type type, Map<String, Type> typeTable)
        { return this; }
        @Override public Member getMember(String name, Map<String, Type> typeTable) { return null; }
        @Override public boolean canItemAccess() { return true; }
        @Override public boolean isCallable() { return true; }
    }

    private static class TypeBuilder {
        private final String name;
        private final String displayName;
        private String superType;
        private final Map<String, Member> members = new HashMap<>();
        private final Map<String, Map<Operation, String>> operations = new HashMap<>();
        private boolean itemAccessible = false;
        private boolean callable;
        private TypeBuilder(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
            addOperation(Operation.ADD, "String", "String");
        }
        private TypeBuilder(String name){
            this(name, name);
        }
        public TypeBuilder addMember(Member member){
            this.members.put(member.name, member);
            return this;
        }
        public TypeBuilder addOperation(Operation operation, String with, String returnType){
            Map<Operation, String> lowerOpMap = operations.computeIfAbsent(with, k -> new HashMap<>());
            lowerOpMap.put(operation, returnType);
            return this;
        }
        public TypeBuilder setItemAccessible(boolean accessible){
            this.itemAccessible = accessible;
            return this;
        }
        public TypeBuilder setCallable(boolean flag){
            this.callable = flag;
            return this;
        }
        public TypeBuilder setSuperType(String name){
            this.superType = name;
            return this;
        }

        public Type create(){
            return new Type() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public String getPrintName() {
                    return displayName;
                }

                @Override
                public Type performOperation(Operation op, Type type, Map<String, Type> typeTable) {
                    Map<Operation, String> lowerOpMap = operations.get(type.getName());
                    if (lowerOpMap == null)
                        return null;
                    String returnType = lowerOpMap.get(op);
                    if (returnType == null)
                        return UnknownType.INSTANCE;
                    return typeTable.get(returnType);
                }

                @Override
                public Member getMember(String name, Map<String, Type> typeTable) {
                    Member member = members.get(name);
                    if (member != null && superType == null) return member;
                    Type superClass = typeTable.get(superType);
                    if (superType == null) return null;
                    return superClass.getMember(name, typeTable);
                }

                @Override
                public boolean canItemAccess() {
                    return itemAccessible;
                }

                @Override
                public boolean isCallable() {
                    return callable;
                }

                @Override
                public String toString() {
                    return name;
                }
            };
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
                                        new WarningMessage("unreachable statement", new Fix(new RemoveTextFix("remove unreachable statement"), "")),
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

        private Symbol(String name, Visibility visibility, PsiElement element, Symbol.Kind kind, Scope.Kind where) {
            this.name = name;
            this.visibility = visibility;
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
        public final Visibility visibility;
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

        abstract AnnotationBuilder createAnnotationBuilder(PsiElementInfo info, AnnotationHolder holder);
    }

    private static class ErrorMessage extends HoverMessage {

        public ErrorMessage(String message, Fix quickFix) {
            super(message, quickFix);
        }

        @Override
        AnnotationBuilder createAnnotationBuilder(PsiElementInfo info, AnnotationHolder holder) {
            return holder.newAnnotation(HighlightSeverity.ERROR, message)
                    .highlightType(ProblemHighlightType.ERROR)
                    .range(info.element);
        }
    }

    private static class WarningMessage extends HoverMessage {

        public WarningMessage(String message, Fix quickFix) {
            super(message, quickFix);
        }

        @Override
        AnnotationBuilder createAnnotationBuilder(PsiElementInfo info, AnnotationHolder holder) {
            return holder.newAnnotation(HighlightSeverity.WARNING, message)
                    .highlightType(ProblemHighlightType.WARNING)
                    .range(info.element);
        }
    }

    private static class WeakWarningMessage extends HoverMessage {

        public WeakWarningMessage(String message, Fix quickFix) {
            super(message, quickFix);
        }

        @Override
        AnnotationBuilder createAnnotationBuilder(PsiElementInfo info, AnnotationHolder holder) {
            return holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
                    .highlightType(ProblemHighlightType.WARNING)
                    .range(info.element);
        }
    }


    private static class LinkToDuplicateFix implements LocalQuickFix {

        private final PsiElement element;

        private LinkToDuplicateFix(PsiElement element) {
            this.element = element;
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return "Navigate to previous definition";
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            if (element instanceof Navigatable n)
                n.navigate(true);
        }
    }


    private static class PsiPtr implements SmartPsiElementPointer<PsiElement> {

        private final PsiElement element;

        private PsiPtr(PsiElement element) {
            this.element = element;
        }

        @Override
        public @Nullable PsiElement getElement() {
            return element;
        }

        @Override
        public @Nullable PsiFile getContainingFile() {
            return element.getContainingFile();
        }

        @Override
        public @NotNull Project getProject() {
            return element.getProject();
        }

        @Override
        public VirtualFile getVirtualFile() {
            return element.getContainingFile().getVirtualFile();
        }

        @Override
        public @Nullable Segment getRange() {
            return element.getTextRange();
        }

        @Override
        public @Nullable Segment getPsiRange() {
            return element.getTextRange();
        }
    }

    private static class RemoveTextFix implements LocalQuickFix {

        private final String text;

        @SafeFieldForPreview
        private PsiPtr toRemove;

        private RemoveTextFix(String text) {
            this.text = text;
        }

        private RemoveTextFix(String text, PsiElement removal){
            this(text);
            this.toRemove = new PsiPtr(removal);
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return text;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            if (toRemove != null) {
                if (toRemove.element != null && toRemove.element.isValid()){
                    toRemove.element.delete();
                }
                return;
            }

            PsiElement element = descriptor.getPsiElement();
            if (element != null && element.isValid()){
                element.delete();
            }
        }
    }

    private static class AddCodeFix implements LocalQuickFix {

        private final String text;
        private final Runner runner;

        public interface Runner {
            void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor);
        }

        private AddCodeFix(String text, Runner runner) {
            this.text = text;
            this.runner = runner;
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return text;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            runner.applyFix(project, descriptor);
        }
    }

    private static ProblemDescriptor makeDescriptor(PsiElement element, String description, LocalQuickFix... additionalFixes){
        return new ProblemDescriptorBase(element, element, description,  additionalFixes, ProblemHighlightType.ERROR, false, null, true, true);
    }
}

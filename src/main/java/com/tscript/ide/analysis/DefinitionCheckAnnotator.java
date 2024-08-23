package com.tscript.ide.analysis;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.annotation.*;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.*;
import com.tscript.ide.analysis.fixes.LinkToDuplicateFix;
import com.tscript.ide.analysis.fixes.RemoveTextFix;
import com.tscript.ide.analysis.hierarchy.Hierarchy;
import com.tscript.ide.analysis.symtab.ContinueAction;
import com.tscript.ide.analysis.symtab.Scope;
import com.tscript.ide.analysis.symtab.Symbol;
import com.tscript.ide.analysis.symtab.Table;
import com.tscript.ide.analysis.typing.*;
import com.tscript.ide.analysis.utils.Visibility;
import com.tscript.ide.highlight.TscriptSyntaxHighlighter;
import com.tscript.ide.psi.*;
import com.tscript.ide.run.build.BuildTscriptTask;
import com.tscript.ide.settings.WebSelectAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;
import java.util.function.Function;

final class DefinitionCheckAnnotator implements Annotator {

    private static final Set<String> BUILT_IN_FUNCTIONS = Set.of("print", "exit", "error", "assert");
    private static final Set<String> BUILT_IN_TYPES =
            Set.of("Integer", "Real", "Boolean", "String", "Null", "Array", "Dictionary", "Range", "Function", "Type");
    private static final Set<String> BUILT_IN_NSPACES = Set.of("math", "turtle", "canvas");

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

        SymbolResolver symbolResolver = new SymbolResolver(holder);
        file.accept(symbolResolver);
        Table table = new Table(symbolResolver.scope);

        HierarchyResolver hierarchyResolver = new HierarchyResolver(table);
        file.accept(hierarchyResolver);
        Hierarchy hierarchy = hierarchyResolver.hierarchy;

        file.accept(new DefinitionChecker(holder, table, hierarchy));
        file.accept(new DependencyChecker(holder, table, hierarchy));
    }

    private static class SymbolResolver extends TestVisitor {

        public Scope scope;
        private Visibility currentVisibility;
        private final AnnotationHolder holder;

        private SymbolResolver(AnnotationHolder holder) {
            this.holder = holder;
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            scope = new Scope(Scope.Kind.GLOBAL, file);

            // LOAD BUILT-INS
            BUILT_IN_FUNCTIONS.forEach(func -> putIfAbsent(scope, func, null, file, Symbol.Kind.FUNCTION, null, false));
            BUILT_IN_TYPES.forEach(type -> putIfAbsent(scope, type, null, file, Symbol.Kind.CLASS, null, false));
            BUILT_IN_NSPACES.forEach(nspace -> putIfAbsent(scope, nspace, null, file, Symbol.Kind.NAMESPACE, null, false));

            file.acceptChildren(this);
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            putIfAbsent(scope, o.getName(), currentVisibility, o.getNameIdentifier(), Symbol.Kind.CLASS, TscriptSyntaxHighlighter.CLASS_DEF_NAME, o.getStaticElement() != null);
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
            putIfAbsent(scope, o.getName(), currentVisibility, o.getNameIdentifier(), Symbol.Kind.NAMESPACE, null, o.getStaticElement() != null);
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
                    LazyAnnotationBuilder.errorAnnotator(holder, o.getAbstractElement(), true, "web tscript does not support keyword 'const'").create();
                }
                if (o.getNativeElement() != null) {
                    LazyAnnotationBuilder.errorAnnotator(holder, o.getNativeElement(), true, "web tscript does not support keyword 'native'").create();
                }
                if (o.getOverriddenElement() != null) {
                    LazyAnnotationBuilder.errorAnnotator(holder, o.getOverriddenElement(), true, "web tscript does not support keyword 'overridden'").create();
                }
            }
            putIfAbsent(scope, o.getName(), currentVisibility, o.getNameIdentifier(), Symbol.Kind.FUNCTION, TscriptSyntaxHighlighter.FUNC_DEF_NAME, o.getStaticElement() != null);
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
                putIfAbsent(scope, o.getName(), null, o.getNameIdentifier(), Symbol.Kind.VARIABLE, null, false);
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
                    putIfAbsent(scope, o.getName(), null, o.getNameIdentifier(), Symbol.Kind.VARIABLE, null, false);
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
            if (scope.kind == Scope.Kind.CLASS) key = TscriptSyntaxHighlighter.MEMBER_REF_NAME;
            for (TestSingleVar s : o.getSingleVarList()) {
                putIfAbsent(scope, s.getName(), currentVisibility, s.getNameIdentifier(), Symbol.Kind.VARIABLE, key, o.getStaticElement() != null);
                if (s.getExpr() != null)
                    s.getExpr().accept(this);
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            if (targetWebTscript){
                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "web tscript does not support keyword 'const'").create();
            }
            TextAttributesKey key = null;
            if (scope.kind == Scope.Kind.CLASS) key = TscriptSyntaxHighlighter.MEMBER_REF_NAME;
            for (TestSingleConst s : o.getSingleConstList()) {
                putIfAbsent(scope, s.getName(), currentVisibility, s.getNameIdentifier(), Symbol.Kind.CONSTANT, key, o.getStaticElement() != null);
                if (s.getExpr() != null)
                    s.getExpr().accept(this);
            }

        }

        @Override
        public void visitParam(@NotNull TestParam o) {
            putIfAbsent(scope, o.getName(),null, o.getNameIdentifier(), Symbol.Kind.VARIABLE, null, false);
        }

        @Override
        public void visitImportStmt(@NotNull TestImportStmt o) {
            if (o.getChainableIdentifier() == null) return;
            List<TestIdentifier> list = o.getChainableIdentifier().getIdentifierList();
            TestIdentifier last = list.get(list.size() - 1);
            putIfAbsent(scope, last.getName(), null, last, Symbol.Kind.UNKNOWN, null, false);
        }

        @Override
        public void visitFromImport(@NotNull TestFromImport o) {
            if (o.getChainableIdentifierList().size() != 2) return;
            List<TestIdentifier> list = o.getChainableIdentifierList().get(1).getIdentifierList();
            TestIdentifier last = list.get(list.size() - 1);
            putIfAbsent(scope, last.getName(), null, last, Symbol.Kind.UNKNOWN, null, false);
        }

        @Override
        public void visitUseStmt(@NotNull TestUseStmt o) {
        }

        @Override
        public void visitFromUse(@NotNull TestFromUse o) {
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            Scope previous = scope;
            scope = new Scope(Scope.Kind.LAMBDA, scope, o);
            previous.children.put(o, scope);

            for (TestClosure closure : o.getClosureList()) {
                if (closure.getExpr() != null)
                    closure.getExpr().accept(this);
                putIfAbsent(scope, closure.getName(), null, closure, Symbol.Kind.VARIABLE, null, false);
            }

            for (TestParam param : o.getParamList()) {
                param.accept(this);
            }

            if (o.getBlock() != null)
                o.getBlock().accept(this);

            scope = previous;
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

        private void putIfAbsent(Scope scope, String name, Visibility visibility, PsiElement element, Symbol.Kind kind, TextAttributesKey extraKey, boolean isStatic){
            Scope curr = scope;
            Set<TextAttributesKey> keys =
                    extraKey != null
                    ? new HashSet<>(Set.of(extraKey))
                    : new HashSet<>();

            do {
                if (curr.table.containsKey(name)){
                    LazyAnnotationBuilder.errorAnnotator(holder, element, true, "web tscript does not support keyword 'const'")
                            .addLocalQuickFix(new LinkToDuplicateFix(curr.table.get(name).element))
                            .create();
                    return;
                }

                if (curr.parent != null && (curr.kind == Scope.Kind.BLOCK || curr.kind == Scope.Kind.GLOBAL)) {
                    curr = curr.parent;
                    continue;
                }

                break;
            }
            while (true);

            scope.table.put(name, new Symbol(name, visibility, element, kind, scope.kind, isStatic));
            LazyAnnotationBuilder.setTextStyle(holder, element, keys);
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

        private boolean usedUseKeywordInGlobalScope = false;
        private boolean inStaticFunction = false;
        private final AnnotationHolder holder;
        private final Table table;
        private final Hierarchy hierarchy;
        private final Deque<TestClassDef> classStack = new ArrayDeque<>();
        private String currentDefined = null;

        private DefinitionChecker(AnnotationHolder holder, Table table, Hierarchy hierarchy) {
            this.holder = holder;
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
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            for (TestClosure closure : o.getClosureList()){
                if (closure.getExpr() == null)
                    handleNamedElement(closure);
                else
                    closure.getExpr().accept(this);
            }
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
        public void visitIdentifier(@NotNull TestIdentifier o) {
            handleNamedElement(o);
        }

        public void handleNamedElement(@NotNull PsiNamedElement u) {
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
                                        LazyAnnotationBuilder.setTextStyle(holder, u, Set.of(TscriptSyntaxHighlighter.MEMBER_REF_NAME));
                                    }
                                }

                                @Override
                                public void visitSingleConst(@NotNull TestSingleConst o) {
                                    if (o.getName() != null && o.getName().equals(name)) {
                                        superMember[0] = new Member(o.getName(), visibility,"var", false);
                                        superMemberKind[0] = Symbol.Kind.VARIABLE;
                                        LazyAnnotationBuilder.setTextStyle(holder, u, Set.of(TscriptSyntaxHighlighter.MEMBER_REF_NAME));
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
                                        LazyAnnotationBuilder.setTextStyle(holder, u, Set.of(TscriptSyntaxHighlighter.CLASS_REF_NAME));
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
                    Set<TextAttributesKey> styles = new HashSet<>(Set.of(TscriptSyntaxHighlighter.ERROR_UNDERLINE));
                    if (superMemberKind[0] == Symbol.Kind.VARIABLE)
                        styles.add(TscriptSyntaxHighlighter.MEMBER_REF_NAME);

                    if (superMember[0].visibility() == Visibility.PRIVATE) {
                        LazyAnnotationBuilder.errorAnnotator(holder, u, true, superMember[0].name() + " has private access")
                                .addTextStyles(styles)
                                .create();
                    }
                    else {
                        verifyValidStaticFieldAccess(superMember[0], u, styles);
                    }
                }
                else if (!usedUseKeywordInGlobalScope){
                    // not found at all
                    LazyAnnotationBuilder.errorAnnotator(holder, u, false, "can not find '" + name + "'").create();
                }
            }
            else if (symbol.where == Scope.Kind.CLASS && (symbol.kind == Symbol.Kind.VARIABLE || symbol.kind == Symbol.Kind.CONSTANT)){
                LazyAnnotationBuilder.setTextStyle(holder, u, Set.of(TscriptSyntaxHighlighter.MEMBER_REF_NAME));

                Member member = new Member(symbol.name, symbol.visibility, "", symbol.isStatic);
                verifyValidStaticFieldAccess(member, u, new HashSet<>(Set.of(TscriptSyntaxHighlighter.MEMBER_REF_NAME)));
            }
            else if (symbol.where == Scope.Kind.GLOBAL && BUILT_IN_TYPES.contains(name)){
                LazyAnnotationBuilder.setTextStyle(holder, u, Set.of(TscriptSyntaxHighlighter.CLASS_REF_NAME));
            }
            else if (symbol.where == Scope.Kind.GLOBAL && BUILT_IN_FUNCTIONS.contains(name)){
                LazyAnnotationBuilder.setTextStyle(holder, u, Set.of(TscriptSyntaxHighlighter.BUILTIN_REF_NAME));
            }
            else if (currentDefined != null && currentDefined.equals(u.getName())){
                LazyAnnotationBuilder.errorAnnotator(holder, u, true, "can not use " + currentDefined + " before it is defined").create();
            }

        }

        private void verifyValidStaticFieldAccess(Member member, PsiElement caller, Set<TextAttributesKey> styles){
            if (!member.isStatic() && inStaticFunction){
                LazyAnnotationBuilder.errorAnnotator(holder, caller, true, "can not access non static member from a static context")
                        .addTextStyles(styles)
                        .create();
            }
        }

        @Override
        public void visitChainableIdentifier(@NotNull TestChainableIdentifier o) {
            o.getIdentifierList().get(0).accept(this);
        }

        @Override
        public void visitFromImport(@NotNull TestFromImport o) {
            // avoid this. only refers to outer/uncheckable code
        }

        @Override
        public void visitImportStmt(@NotNull TestImportStmt o) {
        }

        @Override
        public void visitUseStmt(@NotNull TestUseStmt o) {
            if (o.getChainableIdentifier() == null) return;
            o.getChainableIdentifier().accept(this);
            if (table.currentScope.kind == Scope.Kind.GLOBAL)
                usedUseKeywordInGlobalScope = true;
        }

        @Override
        public void visitFromUse(@NotNull TestFromUse o) {
            if (o.getChainableIdentifierList().isEmpty()) return;
            o.getChainableIdentifierList().get(0).accept(this);
            if (table.currentScope.kind == Scope.Kind.GLOBAL)
                usedUseKeywordInGlobalScope = true;
        }

        @Override
        public void visitSuperAccess(@NotNull TestSuperAccess o) {
            if (inCall(o)) return;
            LazyAnnotationBuilder.setTextStyle(holder, o.getNameIdentifier(), Set.of(TscriptSyntaxHighlighter.MEMBER_REF_NAME));
        }


        @Override
        public void visitMemAccess(@NotNull TestMemAccess o) {
            if (inThisAccess(o)){

                Function<Scope, ContinueAction> searchInThisClass = scope -> {
                    if (scope.kind == Scope.Kind.GLOBAL) return ContinueAction.SUCCESS;
                    if (scope.kind == Scope.Kind.LAMBDA) return ContinueAction.STOP;
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
                    LazyAnnotationBuilder.errorAnnotator(holder, o.getNameIdentifier(), false, "can not find '" + o.getName() + "'").create();
                    return;
                }
            }

            if (inCall(o)) return;
            LazyAnnotationBuilder.setTextStyle(holder, o.getNameIdentifier(), Set.of(TscriptSyntaxHighlighter.MEMBER_REF_NAME));
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
                LazyAnnotationBuilder.errorAnnotator(holder, o.getNativeElement(), true, "native function must not have a body")
                        .addLocalQuickFix(new RemoveTextFix("make function not native", o.getNativeElement()))
                        .create();
            }
            else if (o.getAbstractElement() != null && o.getBlock() != null){
                LazyAnnotationBuilder.errorAnnotator(holder, o.getNativeElement(), true, "abstract function must not have a body")
                        .addLocalQuickFix(new RemoveTextFix("make function not abstract", o.getNativeElement()))
                        .create();
            }
            else if (o.getBlock() == null && o.getNativeElement() == null && o.getAbstractElement() == null){
                LazyAnnotationBuilder.errorAnnotator(holder, o.getNameIdentifier(), true, "missing function body")
                        .addTextStyles(Set.of(TscriptSyntaxHighlighter.FUNC_DEF_NAME))
                        .create();
            }

            boolean prevInStatic = this.inStaticFunction;
            this.inStaticFunction = o.getStaticElement() != null;

            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();

            this.inStaticFunction = prevInStatic;
        }

        @Override
        public void visitConstructorDef(@NotNull TestConstructorDef o) {
            table.enterScope(o);
            o.acceptChildren(this);
            table.leaveScope();
        }

    }


    private static class DependencyChecker extends TestVisitor {

        private final AnnotationHolder holder;
        private final Table table;
        private final Hierarchy hierarchy;
        private final LinkedList<String> accessDepth = new LinkedList<>();

        private final Map<Symbol, Symbol> inheritanceMap = new HashMap<>();

        public DependencyChecker(AnnotationHolder holder, Table table, Hierarchy hierarchy) {
            this.holder = holder;
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
                    LazyAnnotationBuilder.errorAnnotator(holder, curr.element, true, "infinite inheritance cycle").create();
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
                    LazyAnnotationBuilder.warningAnnotation(holder, superIdent.getIdentifierList().get(0), o.getName() + " inherits a builtin-type").create();
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

                        LazyAnnotationBuilder.errorAnnotator(holder, ident, false, leastValid + " has no class member " + list.get(failIndex)).create();
                    }
                    else if (sym.kind != Symbol.Kind.CLASS){
                        StringBuilder fullName = new StringBuilder();
                        Iterator<String> iter = list.iterator();
                        fullName.append(iter.next());

                        for (int i = 1; i < list.size(); i++) {
                            fullName.append(".").append(iter.next());
                        }

                        LazyAnnotationBuilder.errorAnnotator(holder, o.getSuper(), true, fullName + " is not a class").create();
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
                if (classDef.getAbstractElement() != null && o.getNextSibling() instanceof TestCall){
                    LazyAnnotationBuilder.errorAnnotator(holder, o, true, "can not instantiate abstract class")
                            .addLocalQuickFix(new RemoveTextFix("make '" + o.getName() + "' not abstract", classDef.getAbstractElement()))
                            .create();
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
                                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot reference 'this' before supertype constructor has been called").create();
                            }

                            @Override
                            public void visitSuperAccess(@NotNull TestSuperAccess o) {
                                LazyAnnotationBuilder.errorAnnotator(holder, o.getNameIdentifier(), true, "Cannot reference '" + o.getName() + "' before supertype constructor has been called").create();
                                o.acceptChildren(this);
                            }

                            @Override
                            public void visitIdentifier(@NotNull TestIdentifier o) {
                                Symbol globalSym = table.root.table.get(o.getName());
                                if (globalSym != null){
                                    if (!paramList.contains(o.getName()) && (globalSym.kind == Symbol.Kind.VARIABLE || globalSym.kind == Symbol.Kind.CONSTANT)) {
                                        LazyAnnotationBuilder.weakWarningAnnotation(holder, o, o.getName() + " may not have been initialized").create();
                                    }
                                }
                                else if (!paramList.contains(o.getName())){
                                    LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot reference '" + o.getName() + "' before supertype constructor has been called").create();
                                }
                            }
                        });
                    }
                }
            }

            if (o.getBlock() != null)
                o.getBlock().accept(this);
        }

    }

}

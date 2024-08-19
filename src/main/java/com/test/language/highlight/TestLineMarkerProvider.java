package com.test.language.highlight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.test.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TestLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        element.accept(new ActionResolver(result));
    }

    private static class PsiAction extends TestVisitor {
        protected final Collection<? super RelatedItemLineMarkerInfo<?>> result;
        private PsiAction(Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            this.result = result;
        }
    }



    private static class ActionResolver extends PsiAction {
        private ActionResolver(Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            o.accept(new IdentifierHandler(result));
        }

        @Override
        public void visitThisExpr(@NotNull TestThisExpr o) {
            if (o.getNextSibling() instanceof TestCall){
                o.getParent().getParent().accept(new RecursionChecker(o, result));
            }
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (o.getSuper() != null){

                SuperClassResolver resolver = new SuperClassResolver(result);
                o.getSuper().accept(resolver);

                if (resolver.resolvedSuperClass != null) {
                    result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridenMethod)
                            .setTargets(List.of(Objects.requireNonNull(o.getSuper())))
                            .setTooltipText("Is subclassed")
                            .createLineMarkerInfo(Objects.requireNonNull(resolver.resolvedSuperClass.getNameIdentifier())));
                }
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (o.getName() != null){
                o.accept(new OverridingChecker(result));
            }
        }
    }



    private static class IdentifierHandler extends PsiAction {

        private IdentifierHandler(Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            if (o.getName() != null && o.getNextSibling() instanceof TestCall){
                o.getParent().getParent().accept(new RecursionChecker(o, result));
            }
        }
    }



    private static class RecursionChecker extends PsiAction {

        private final PsiElement element;
        private RecursionChecker(PsiElement element, Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
            this.element = element;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.getParent().accept(this);
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
        }

        @Override
        public void visitConstructorDef(@NotNull TestConstructorDef o) {
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            if (!(element instanceof TestThisExpr)) return;
            result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.RecursiveMethod)
                    .setTargets(List.of(o))
                    .setTooltipText("Recursive call")
                    .createLineMarkerInfo(element));
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (o.getName() != null && element instanceof TestIdentifier i && o.getName().equals(i.getName())) {
                result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.RecursiveMethod)
                                .setTargets(List.of(Objects.requireNonNull(o.getNameIdentifier())))
                                .setTooltipText("Recursive call")
                                .createLineMarkerInfo(Objects.requireNonNull(i.getNameIdentifier())));
            }
        }
    }


    private static class SuperClassResolver extends PsiAction {

        private Iterator<String> accessItr;
        private String next;
        public TestClassDef resolvedSuperClass;

        private SuperClassResolver(Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
        }

        @Override
        public void visitChainableIdentifier(@NotNull TestChainableIdentifier o) {

            List<String> accessChain = new ArrayList<>();

            for (TestIdentifier ident : o.getIdentifierList()){
                if (ident.getName() == null) return;
                accessChain.add(ident.getName());
            }

            accessItr = accessChain.iterator();
            if (!accessItr.hasNext()) return;
            next = accessItr.next();

            PsiFile topLevel = o.getContainingFile();
            try {
                topLevel.acceptChildren(this);
            }
            catch (ProcessCanceledException ignored) {}
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {

            if (o.getName() == null || !o.getName().equals(next)) return;

            if (!accessItr.hasNext()) {
                resolvedSuperClass = o;
                throw new ProcessCanceledException();
            }

            next = accessItr.next();
            for (TestClassDef nestedClass : o.getClassDefList()){
                nestedClass.accept(this);
            }
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            if (o.getName() == null || !o.getName().equals(next)) return;
            if (!accessItr.hasNext()) return;

            next = accessItr.next();
            for (TestClassDef nestedClass : o.getClassDefList()){
                nestedClass.accept(this);
            }
        }
    }


    private static class OverridingChecker extends PsiAction {

        private TestFunctionDef checked;

        private OverridingChecker(Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            checked = o;

            PsiElement current = o.getParent();
            while (current != null && !(current instanceof TestClassDef))
                current = current.getParent();

            if (current == null)
                // in global scope. No overriding
                return;

            TestClassDef classDef = (TestClassDef) current;
            if (classDef.getSuper() == null) return;

            SuperClassResolver resolver = new SuperClassResolver(result);
            classDef.getSuper().accept(resolver);
            if (resolver.resolvedSuperClass != null) {
                resolver.resolvedSuperClass.acceptChildren(new OverriddenSearcher());
            }

        }


        private class OverriddenSearcher extends TestVisitor {

            @Override
            public void visitFunctionDef(@NotNull TestFunctionDef o) {
                if (o.getName() == null) return;
                if (!o.getName().equals(checked.getName())) return;

                result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridenMethod)
                        .setTargets(List.of(Objects.requireNonNull(checked.getNameIdentifier())))
                        .setTooltipText("Overridden in subclass")
                        .createLineMarkerInfo(Objects.requireNonNull(o.getNameIdentifier())));

                result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridingMethod)
                        .setTargets(List.of(Objects.requireNonNull(o.getNameIdentifier())))
                        .setTooltipText("Overrides function in superclass")
                        .createLineMarkerInfo(Objects.requireNonNull(checked.getNameIdentifier())));
            }
        }
    }

}

package com.tscript.ide.highlight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TscriptLineMarkerProvider extends RelatedItemLineMarkerProvider {

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
            TestClassDef superClass = TscriptASTUtils.getSuperClass(o);

            if (superClass != null) {
                result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridenMethod)
                        .setTargets(List.of(Objects.requireNonNull(o.getSuper())))
                        .setTooltipText("Is subclassed")
                        .createLineMarkerInfo(Objects.requireNonNull(superClass.getNameIdentifier())));
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

    private static class OverridingChecker extends PsiAction {

        private TestFunctionDef checked;

        private OverridingChecker(Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            checked = o;

            TestClassDef classDef = TscriptASTUtils.getCurrentClass(o);

            if (classDef == null) return;
            if (classDef.getSuper() == null) return;

            TestClassDef superClass = TscriptASTUtils.getSuperClass(classDef);
            if (superClass != null && superClass.getClassBodyDef() != null) {
                superClass.getClassBodyDef().acceptChildren(new OverriddenSearcher());
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

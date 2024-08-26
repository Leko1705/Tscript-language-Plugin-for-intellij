package com.tscript.ide.highlight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
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
                try {
                    o.getParent().accept(new RecursionChecker(o, result));
                }
                catch (Stop ignored){}
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
                TestFunctionDef overridden = TscriptASTUtils.getOverriddenFunction(o);
                if (overridden != null){
                    result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridenMethod)
                            .setTargets(List.of(Objects.requireNonNull(o.getNameIdentifier())))
                            .setTooltipText("Overridden in subclass")
                            .createLineMarkerInfo(Objects.requireNonNull(overridden.getNameIdentifier())));

                    result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridingMethod)
                            .setTargets(List.of(Objects.requireNonNull(overridden.getNameIdentifier())))
                            .setTooltipText("Overrides function in superclass")
                            .createLineMarkerInfo(Objects.requireNonNull(o.getNameIdentifier())));
                }
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
                try {
                    o.getParent().accept(new RecursionChecker(o, result));
                }
                catch (Stop ignored){}
            }
        }
    }


    private static class Stop extends RuntimeException{}


    private static class RecursionChecker extends PsiAction {

        private final PsiElement element;

        private RecursionChecker(PsiElement element, Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            super(result);
            this.element = element;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (element.getPrevSibling() != null)
                element.getPrevSibling().accept(this);
            else
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
        public void visitStmt(@NotNull TestStmt o) {
            if (o.getVarDec() != null) {
                for (TestSingleVar var : o.getVarDec().getSingleVarList())
                    visitSingleVar(var);
            }
            else if (o.getConstDec() != null) {
                for (TestSingleConst var : o.getConstDec().getSingleConstList())
                    visitSingleConst(var);
            }
            visitElement(o);
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            if (element instanceof TestIdentifier i && i.getName() != null && i.getName().equals(o.getName())){
                throw new Stop();
            }
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            if (element instanceof TestIdentifier i && i.getName() != null && i.getName().equals(o.getName())){
                throw new Stop();
            }
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

}

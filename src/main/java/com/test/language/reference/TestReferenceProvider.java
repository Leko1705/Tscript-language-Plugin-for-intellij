package com.test.language.reference;

import com.intellij.codeInsight.highlighting.HighlightedReference;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.test.language.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TestReferenceProvider extends PsiReferenceProvider {

    @Override
    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        TestIdentifier literalExpression = (TestIdentifier) element;
        String value = literalExpression.getName();

        if (value != null) {
            return new PsiReference[]{new Reference(literalExpression)};
        }
        return PsiReference.EMPTY_ARRAY;
    }


    private static class Reference extends PsiReferenceBase<TestIdentifier> implements PsiPolyVariantReference, HighlightedReference {

        public Reference(@NotNull TestIdentifier element) {
            super(element, element.getTextRange());
        }

        @Override
        public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
            TestIdentifier ident = getElement();
            if (ident.getName() == null) return ResolveResult.EMPTY_ARRAY;
            Supplier<List<ResolveResult>> supplier = computeAction(getElement());
            return supplier.get().toArray(new ResolveResult[0]);
        }

        @Override
        public @Nullable PsiElement resolve() {
            ResolveResult[] resolveResults = multiResolve(false);
            return resolveResults.length == 1 ? resolveResults[0].getElement() : getElement();
        }

        @SuppressWarnings("unchecked")
        private Supplier<List<ResolveResult>> computeAction(TestIdentifier element){
            PsiElement current = element;
            Supplier<List<ResolveResult>>[] supplier = new Supplier[1];

            while (current != null && supplier[0] == null) {

                PsiElement prev = current.getPrevSibling();
                current = (prev != null) ? prev : current.getParent();
                current.accept(new TestVisitor(){

                    @Override
                    public void visitVarDec(@NotNull TestVarDec o) {
                        for (TestSingleVar singleVar : o.getSingleVarList()){
                            if (singleVar.getName() == null) continue;
                            if (singleVar.getName().equals(element.getName())){
                                supplier[0] = new Reference.LocalResolveAction(o);
                            }
                        }
                    }

                    @Override
                    public void visitConstDec(@NotNull TestConstDec o) {
                        for (TestSingleConst singleVar : o.getSingleConstList()){
                            if (singleVar.getName() == null) continue;
                            if (singleVar.getName().equals(element.getName())){
                                supplier[0] = new Reference.LocalResolveAction(o);
                            }
                        }
                    }

                    @Override
                    public void visitFunctionDef(@NotNull TestFunctionDef o) {
                        for (TestParam param : o.getParamList()){
                            if (param.getName() == null) continue;
                            if (param.getName().equals(element.getName())){
                                supplier[0] = new Reference.LocalResolveAction(o);
                            }
                        }
                    }

                });


            }

            return supplier[0];
        }


        private class LocalResolveAction implements Supplier<List<ResolveResult>> {

            private final PsiElement defElement;

            private LocalResolveAction(PsiElement defElement) {
                this.defElement = defElement;
            }

            @Override
            public List<ResolveResult> get() {
                List<ResolveResult> results = new ArrayList<>();
                PsiElement current = defElement;

                while (current != null) {
                    Reference.LocalResolver resolver = new Reference.LocalResolver();
                    current.accept(resolver);
                    results.addAll(resolver.results);
                    current = current.getNextSibling();
                }

                return results;
            }
        }


        private class LocalResolver extends TestVisitor {

            private final List<ResolveResult> results = new ArrayList<>();

            @Override
            public void visitElement(@NotNull PsiElement element) {
                element.acceptChildren(this);
            }

            @Override
            public void visitIdentifier(@NotNull TestIdentifier o) {
                if (myElement.getName() != null && o.getName() == null && myElement.getName().equals(o.getName()))
                    results.add(new PsiElementResolveResult(o));
            }
        }
    }
}

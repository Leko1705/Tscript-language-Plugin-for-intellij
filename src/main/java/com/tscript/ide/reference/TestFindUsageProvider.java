package com.tscript.ide.reference;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import com.tscript.ide.TestLexerAdapter;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestFindUsageProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new TestLexerAdapter(),
                TokenSet.create(TestTypes.IDENT, TestTypes.CHAINABLE_IDENTIFIER),
                TokenSet.create(TestTypes.COMMENT),
                TokenSet.EMPTY);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return (psiElement instanceof PsiNamedElement e && e.getName() != null)
                || psiElement instanceof TestChainableIdentifier;
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {

        if (element instanceof TestClassDef || element instanceof TestChainableIdentifier){
            return "class";
        }
        if (element instanceof TestFunctionDef){
            return "function";
        }
        if (element instanceof TestNamespaceDef){
            return "namespace";
        }
        if (element instanceof TestSingleVar) {
            return "variable";
        }
        if (element instanceof TestSingleConst) {
            return "constant";
        }
        else if (element instanceof TestIdentifier) {
            return "variable";
        }
        return "";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof PsiNamedElement e && e.getName() != null) {
            return e.getName();
        }
        else if (element instanceof TestChainableIdentifier chain){
            StringBuilder sb = new StringBuilder();
            for (TestIdentifier identifier : chain.getIdentifierList()){
                if (identifier.getName() == null) return "";
                sb.append(identifier.getName());
            }
            return sb.toString();
        }
        return "";
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof PsiNamedElement e && e.getName() != null) {
            return e.getName();
        }
        return "";
    }
}

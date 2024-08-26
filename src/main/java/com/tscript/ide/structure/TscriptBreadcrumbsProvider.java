package com.tscript.ide.structure;

import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider;
import com.tscript.ide.TscriptLanguage;
import com.tscript.ide.psi.TestClassDef;
import com.tscript.ide.psi.TestFunctionDef;
import com.tscript.ide.psi.TestNamespaceDef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class TscriptBreadcrumbsProvider implements BreadcrumbsProvider {

    @Override
    public Language[] getLanguages() {
        return new Language[]{TscriptLanguage.INSTANCE};
    }

    @Override
    public boolean acceptElement(@NotNull PsiElement element) {
        return element instanceof TestClassDef
                || element instanceof TestFunctionDef
                || element instanceof TestNamespaceDef;
    }

    @Override
    public @NotNull @NlsSafe String getElementInfo(@NotNull PsiElement element) {
        if (element instanceof PsiNamedElement n)
            return Objects.requireNonNullElse(n.getName(), "");
        return "";
    }

    @Override
    public @Nullable PsiElement getParent(@NotNull PsiElement element) {
        while (!(element instanceof PsiFile)){
            element = element.getParent();
            if (acceptElement(element))
                return element;
        }
        return null;
    }

    @Override
    public @Nullable Icon getElementIcon(@NotNull PsiElement element) {
        return new TscriptAwareNavBar().getIcon(element);
    }

}

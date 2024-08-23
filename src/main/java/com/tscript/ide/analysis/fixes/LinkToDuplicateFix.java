package com.tscript.ide.analysis.fixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.utils.PsiPtr;
import org.jetbrains.annotations.NotNull;

public class LinkToDuplicateFix implements LocalQuickFix {

    @SafeFieldForPreview
    private final PsiPtr ptr;

    public LinkToDuplicateFix(PsiElement element) {
        this.ptr = new PsiPtr(element);
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return "Navigate to previous definition";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        if (ptr.element instanceof Navigatable n)
            n.navigate(true);
    }
}
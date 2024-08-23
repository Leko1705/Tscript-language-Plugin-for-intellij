package com.tscript.ide.analysis.fixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.utils.PsiPtr;
import org.jetbrains.annotations.NotNull;

public class RemoveTextFix implements LocalQuickFix {

    private final String text;

    @SafeFieldForPreview
    private PsiPtr toRemove;

    public RemoveTextFix(String text) {
        this.text = text;
    }

    public RemoveTextFix(String text, PsiElement removal){
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

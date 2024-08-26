package com.tscript.ide.analysis.fixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MultiFix implements LocalQuickFix {

    private final String name;
    @SafeFieldForPreview
    private final LocalQuickFix[] fixes;

    public MultiFix(String name, LocalQuickFix... fixes) {
        this.name = name;
        this.fixes = fixes;
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return name;
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        for (LocalQuickFix fix : fixes) {
            fix.applyFix(project, descriptor);
        }
    }
}

package com.tscript.ide.analysis;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemDescriptorBase;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;

public class QuickFixDescriptionFactory {

    private QuickFixDescriptionFactory() {}

    public static ProblemDescriptor create(PsiElement element){
        return create(element, "");
    }

    public static ProblemDescriptor create(PsiElement element, String description, LocalQuickFix... additionalFixes){
        return new ProblemDescriptorBase(element, element, description,  additionalFixes, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, false, null, true, true);
    }

}

package com.tscript.ide.analysis.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SmartPsiElementPointer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiPtr implements SmartPsiElementPointer<PsiElement> {

    public final PsiElement element;

    public PsiPtr(PsiElement element) {
        this.element = element;
    }

    @Override
    public @Nullable PsiElement getElement() {
        return element;
    }

    @Override
    public @Nullable PsiFile getContainingFile() {
        return element.getContainingFile();
    }

    @Override
    public @NotNull Project getProject() {
        return element.getProject();
    }

    @Override
    public VirtualFile getVirtualFile() {
        return element.getContainingFile().getVirtualFile();
    }

    @Override
    public @Nullable Segment getRange() {
        return element.getTextRange();
    }

    @Override
    public @Nullable Segment getPsiRange() {
        return element.getTextRange();
    }
}
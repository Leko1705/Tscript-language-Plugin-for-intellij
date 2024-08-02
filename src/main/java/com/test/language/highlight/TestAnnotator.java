package com.test.language.highlight;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.test.language.psi.*;
import org.jetbrains.annotations.NotNull;

final class TestAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TestFunctionDef def){
            PsiElement identifier = def.getNameIdentifier();
            if (identifier != null){
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(identifier)
                        .textAttributes(TestSyntaxHighlighter.FUNC_DEF_NAME)
                        .create();
            }
        }

        if (element instanceof TestClassDef def){
            PsiElement identifier = def.getNameIdentifier();
            if (identifier != null){
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(identifier)
                        .textAttributes(TestSyntaxHighlighter.CLASS_DEF_NAME)
                        .create();
            }

            identifier = def.getSuperClassIdentifier();
            if (identifier != null){
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(identifier)
                        .textAttributes(TestSyntaxHighlighter.CLASS_REF_NAME)
                        .create();
            }
        }
    }

}

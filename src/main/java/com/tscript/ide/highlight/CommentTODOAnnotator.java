package com.tscript.ide.highlight;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.LazyAnnotationBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CommentTODOAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if ((!(element instanceof PsiComment comment))) return;

        String text = comment.getText();
        int todoStart = text.toLowerCase().indexOf("todo");
        if (todoStart == -1) return;

        int todoEnd = text.toLowerCase().indexOf('\n', todoStart);
        if (todoEnd == -1) todoEnd = text.length() - (text.length() - todoStart);

        TextRange range = new TextRange(Math.max(0, element.getTextOffset() + todoStart - 1), element.getTextOffset() + todoEnd);

        LazyAnnotationBuilder.setTextStyle(holder, range, Set.of(TscriptSyntaxHighlighter.TODO_TEXT_COLOR));
    }

}

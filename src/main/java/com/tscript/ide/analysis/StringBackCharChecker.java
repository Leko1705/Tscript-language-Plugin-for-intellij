package com.tscript.ide.analysis;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.tscript.ide.highlight.Styles;
import com.tscript.ide.highlight.TscriptSyntaxHighlighter;
import com.tscript.ide.psi.TestStringExpr;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class StringBackCharChecker implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof TestStringExpr)) return;

        String content = element.getText().substring(0, element.getText().length() - 1);

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '\\') {
                i++;

                if (i >= content.length())
                    return;

                char c2 = content.charAt(i);
                TextRange range = new TextRange(element.getTextOffset() + i - 1, element.getTextOffset() + i + 1);

                if (!Set.of('n', 'b', 't', '\\', 'r', 'u').contains(c2)){
                    holder.newAnnotation(HighlightSeverity.ERROR, "Invalid escape character '\\" + c2 + "'")
                            .range(range)
                            .highlightType(ProblemHighlightType.ERROR)
                            .textAttributes(Styles.mergeAttributes(TscriptSyntaxHighlighter.ERROR_UNDERLINE, TscriptSyntaxHighlighter.ESCAPE_CHARACTER))
                            .create();
                }
                else if (c2 == 'u'){
                    int j = 0;
                    for (; i < 4; i++){
                        if (content.length() >= i + j + 2){
                            errorMissingUnicodeEncodingDigit(element, i, i + j + 1, holder);
                            break;
                        }
                        c2 = content.charAt(i + j + 2);
                        if (Character.isDigit(c2)) continue;
                        if (!Character.isLetter(c2)) errorInvalidHexDigit(element, i, i + j, c2, holder);
                        else if (c2 < 'A' || c2 > 'f') errorInvalidHexDigit(element, i, i + j, c2, holder);
                    }
                    range = new TextRange(range.getStartOffset(), range.getStartOffset() + i + j + 2);
                    LazyAnnotationBuilder.setTextStyle(holder, range, Set.of(TscriptSyntaxHighlighter.ESCAPE_CHARACTER));
                }
                else {
                    LazyAnnotationBuilder.setTextStyle(holder, range, Set.of(TscriptSyntaxHighlighter.ESCAPE_CHARACTER));
                }
            }
        }

    }

    private void errorInvalidHexDigit(PsiElement e, int offs, int n, char c, AnnotationHolder holder){
        holder.newAnnotation(HighlightSeverity.ERROR, "Invalid unicode encoding digit '" + c + "'")
                .range(new TextRange(e.getTextOffset() + offs + n, e.getTextOffset() + offs + n + 1))
                .highlightType(ProblemHighlightType.ERROR)
                .textAttributes(TscriptSyntaxHighlighter.ERROR_UNDERLINE)
                .create();
    }

    private void errorMissingUnicodeEncodingDigit(PsiElement e, int offs, int n, AnnotationHolder holder){
        holder.newAnnotation(HighlightSeverity.ERROR, "Missing encoding digit for unicode character")
                .range(new TextRange(e.getTextOffset() + offs + n, e.getTextOffset() + offs + n + 1))
                .highlightType(ProblemHighlightType.ERROR)
                .textAttributes(TscriptSyntaxHighlighter.ERROR_UNDERLINE)
                .create();
    }

}

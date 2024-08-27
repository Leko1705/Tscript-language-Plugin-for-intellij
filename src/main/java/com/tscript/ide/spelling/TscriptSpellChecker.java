package com.tscript.ide.spelling;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.spellchecker.inspections.PlainTextSplitter;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.tscript.ide.psi.TestStringExpr;
import org.jetbrains.annotations.NotNull;

public class TscriptSpellChecker extends SpellcheckingStrategy {

    @Override
    public @NotNull Tokenizer<?> getTokenizer(PsiElement element) {

        if (element instanceof PsiNamedElement) {
            return new IdentifierTokenizer();
        }

        if (element instanceof TestStringExpr) {
            return new StringTokenizer();
        }

        if (element instanceof PsiComment) {
            return new CommentTokenizer();
        }

        return EMPTY_TOKENIZER;
    }

    private static class StringTokenizer extends Tokenizer<TestStringExpr> {

        @Override
        public void tokenize(@NotNull TestStringExpr element, @NotNull TokenConsumer consumer) {
            String text = element.getText()
                    .substring(1, element.getTextLength()-1)
                    .replaceAll("\\\\n", "\n");

            consumer.consumeToken(
                    element,
                    text,
                    false,
                    0,
                    TextRange.create(1, element.getTextLength() - 2),
                    PlainTextSplitter.getInstance());
        }
    }

    private static class IdentifierTokenizer extends Tokenizer<PsiElement> {
        @Override
        public void tokenize(@NotNull PsiElement element, @NotNull TokenConsumer consumer) {
            consumer.consumeToken(element, PlainTextSplitter.getInstance());
        }
    }

    private static class CommentTokenizer extends Tokenizer<PsiComment> {

        @Override
        public void tokenize(@NotNull PsiComment element, @NotNull TokenConsumer consumer) {
            consumer.consumeToken(element, PlainTextSplitter.getInstance());
        }

    }

}

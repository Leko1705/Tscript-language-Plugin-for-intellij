package com.tscript.ide.spelling;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.inspections.PlainTextSplitter;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

public class TscriptSpellChecker extends SpellcheckingStrategy {

    @Override
    public @NotNull Tokenizer<?> getTokenizer(PsiElement element) {

        if (element instanceof PsiComment) {
            return new CommentTokenizer();
        }

        return EMPTY_TOKENIZER;
    }

    private static class CommentTokenizer extends Tokenizer<PsiComment> {

        @Override
        public void tokenize(@NotNull PsiComment element, @NotNull TokenConsumer consumer) {
            consumer.consumeToken(element, PlainTextSplitter.getInstance());
        }

    }

}

package com.test.language.autocompletion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.test.language.psi.TestTypes;
import org.jetbrains.annotations.NotNull;

final class SimpleCompletionContributor extends CompletionContributor {

    SimpleCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TestTypes.STRING),
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("Hello"));
                    }
                }
        );
    }

}

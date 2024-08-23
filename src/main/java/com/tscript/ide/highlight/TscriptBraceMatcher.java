package com.tscript.ide.highlight;

import com.intellij.codeInsight.highlighting.JavaBraceMatcher;
import com.intellij.codeInsight.highlighting.PairedBraceAndAnglesMatcher;
import com.intellij.codeInsight.hint.DeclarationRangeUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.BracePair;
import com.intellij.lang.Language;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class TscriptBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = new BracePair[] {
            new BracePair(TestTypes.PAREN_OPEN, TestTypes.PAREN_CLOSE, false),
            new BracePair(TestTypes.CURLY_OPEN, TestTypes.CURLY_CLOSE, true),
            new BracePair(TestTypes.BRACKET_OPEN, TestTypes.BRACKET_CLOSE, false)
    };


    @Override
    public BracePair @NotNull [] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return !Set.of(TestTypes.SEMI, TestTypes.IDENT).contains(contextType);
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        PsiElement element = file.findElementAt(openingBraceOffset);

        if (element == null || element instanceof PsiFile) return openingBraceOffset;
        PsiElement parent = element.getParent();

        parent = parent.getParent();

        if (parent instanceof TestFunctionDef
                ||
                parent instanceof TestClassDef
                || parent instanceof TestNamespaceDef) {
            TextRange range = DeclarationRangeUtil.getDeclarationRange(parent);
            return range.getStartOffset();
        }

        return openingBraceOffset;
    }

}

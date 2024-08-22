package com.tscript.ide.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.tscript.ide.TscriptLanguage;
import com.tscript.ide.TscriptLexerAdapter;
import com.tscript.ide.parser.TestParser;
import org.jetbrains.annotations.NotNull;

final class TestParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(TscriptLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new TscriptLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TestTokenSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new TestParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new TestFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return TestTypes.Factory.createElement(node);
    }

}

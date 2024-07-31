package com.test.language;

import com.intellij.lexer.FlexAdapter;

public class TestLexerAdapter extends FlexAdapter {

    public TestLexerAdapter() {
        super(new TestLexer(null));
    }

}
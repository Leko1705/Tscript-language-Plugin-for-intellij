package com.tscript.ide;

import com.intellij.lexer.FlexAdapter;

public class TestLexerAdapter extends FlexAdapter {

    public TestLexerAdapter() {
        super(new TestLexer(null));
    }

}
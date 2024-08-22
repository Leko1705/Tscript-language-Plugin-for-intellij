package com.tscript.ide;

import com.intellij.lexer.FlexAdapter;

public class TscriptLexerAdapter extends FlexAdapter {

    public TscriptLexerAdapter() {
        super(new TestLexer(null));
    }

}
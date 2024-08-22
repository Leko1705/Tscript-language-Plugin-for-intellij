package com.tscript.ide;

import com.intellij.lang.Language;

public class TscriptLanguage extends Language {

    public static final TscriptLanguage INSTANCE = new TscriptLanguage();

    private TscriptLanguage() {
        super("Tscript");
    }

}

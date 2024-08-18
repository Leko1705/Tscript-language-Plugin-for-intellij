package com.test.exec.tscript.tscriptc.tools;

public enum Language {

    TSCRIPT(TscriptCompiler.class),

    TO_WEB_TSCRIPT_CONVERTER(ToWebTscriptConverter.class);

    ;Language(Class<? extends Compiler> clazz){
        this.clazz = clazz;
    }

    final Class<? extends Compiler> clazz;

}

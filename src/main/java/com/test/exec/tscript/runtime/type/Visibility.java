package com.test.exec.tscript.runtime.type;

public enum Visibility {
    PUBLIC,
    PRIVATE,
    PROTECTED;

    public String toString(){
        return super.toString().toLowerCase();
    }

}

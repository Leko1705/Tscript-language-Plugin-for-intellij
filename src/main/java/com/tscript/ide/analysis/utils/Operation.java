package com.tscript.ide.analysis.utils;

public enum Operation {
    ADD("addition"),
    SUB("subtraction"),
    MUL("multiplication"),
    DIV("division"),
    IDIV("integer division"),
    POW("exponentiation"),
    MOD("modulo operation"),
    SAL("left arithmetical shift"),
    SAR("right arithmetical shift"),
    SLR("right logical shift"),
    AND("and"),
    OR("or"),
    XOR("xor"),
    GT("'>'"),
    LT("'<'"),
    GEQ("'>='"),
    LEQ("'<='");

    public final String name;

    Operation(String name) {
        this.name = name;
    }
}
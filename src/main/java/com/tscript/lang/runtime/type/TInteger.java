package com.tscript.lang.runtime.type;

public class TInteger extends PrimitiveObject<Integer> {

    public static final TType TYPE = new TType("Integer", null);

    public TInteger(Integer value) {
        super(value);
    }

    @Override
    public TType getType() {
        return TYPE;
    }
}

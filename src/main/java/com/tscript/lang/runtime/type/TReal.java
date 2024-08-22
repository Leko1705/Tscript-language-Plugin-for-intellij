package com.tscript.lang.runtime.type;

public class TReal extends PrimitiveObject<Double> {

    public static final TType TYPE = new TType("Real", null);

    public TReal(Double value) {
        super(value);
    }

    @Override
    public TType getType() {
        return TYPE;
    }
}

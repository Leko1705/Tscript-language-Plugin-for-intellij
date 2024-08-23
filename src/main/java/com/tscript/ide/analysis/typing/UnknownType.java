package com.tscript.ide.analysis.typing;

import com.tscript.ide.analysis.utils.Operation;

import java.util.Map;

public class UnknownType implements Type {

    public static Type INSTANCE = new UnknownType();

    private UnknownType(){}

    @Override public String getName() {
        return "unknown type";
    }

    @Override public Type performOperation(Operation op, Type type, Map<String, Type> typeTable) {
        return this;
    }

    @Override public Member getMember(String name, Map<String, Type> typeTable) {
        return null;
    }

    @Override public boolean canItemAccess() {
        return true;
    }

    @Override public boolean isCallable() {
        return true;
    }
}

package com.tscript.ide.analysis.typing;

import com.tscript.ide.analysis.utils.Operation;

import java.util.Map;

public class WrappedType implements Type {
    private final Type type;
    public WrappedType(Type type) {
        this.type = type;
    }
    @Override
    public String getName() {
        return "Type<" + type.getName() + ">";
    }
    @Override
    public Type performOperation(Operation op, Type type, Map<String, Type> typeTable) {
        return null;
    }
    @Override
    public Member getMember(String name, Map<String, Type> typeTable) {
        Member member = type.getMember(name, typeTable);

        if (member != null && member.isStatic()){
            return member;
        }

        return null;
    }
    @Override
    public boolean canItemAccess() {
        return false;
    }
    @Override
    public boolean isCallable() {
        return true;
    }
}

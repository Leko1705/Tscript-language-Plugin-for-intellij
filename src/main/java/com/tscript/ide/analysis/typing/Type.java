package com.tscript.ide.analysis.typing;

import com.tscript.ide.analysis.utils.Operation;

import java.util.Map;

public interface Type {

    String getName();

    Type performOperation(Operation op, Type type, Map<String, Type> typeTable);

    Member getMember(String name, Map<String, Type> typeTable);

    boolean canItemAccess();

    boolean isCallable();

    default String getPrintName(){ return getName(); }

    default Type operate(Operation op, Type type, Map<String, Type> typeTable){
        if (type == UnknownType.INSTANCE || !typeTable.containsKey(type.getName()))
            return UnknownType.INSTANCE;
        return performOperation(op, type, typeTable);
    }
}

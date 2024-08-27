package com.tscript.ide.analysis.typing;

import com.tscript.ide.analysis.utils.Operation;
import com.tscript.ide.analysis.utils.Visibility;

import java.util.HashMap;
import java.util.Map;

public class BuiltinTypes {
    
    public static Map<String, Type> get() {
        Map<String, Type> map = new HashMap<>();
        
        registerType(map, new TypeBuilder("Function")
                .setCallable(true)
                .create());

        registerType(map, new TypeBuilder("Type")
                .setCallable(true)
                .addMember(new Member("superclass", Visibility.PUBLIC, "Function", true))
                .addMember(new Member("isOfType", Visibility.PUBLIC, "Function", true))
                .addMember( new Member("isDerivedFrom", Visibility.PUBLIC, "Function", true))
                .create());

        registerType(map, new TypeBuilder("String")
                .setItemAccessible(true)
                .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("find", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("split", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("toLowerCase", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("toUpperCase", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("replace", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("fromUnicode", Visibility.PUBLIC, "Function", true))
                .addMember(new Member("join", Visibility.PUBLIC, "Function", true))
                .create());

        registerType(map, new TypeBuilder("Null", "null").create());

        registerType(map, new TypeBuilder("Integer")
                .addOperation(Operation.ADD, "Integer", "Integer")
                .addOperation(Operation.ADD, "Real", "Real")
                .addOperation(Operation.SUB, "Integer", "Integer")
                .addOperation(Operation.SUB, "Real", "Real")
                .addOperation(Operation.MUL, "Integer", "Integer")
                .addOperation(Operation.MUL, "Real", "Real")
                .addOperation(Operation.DIV, "Integer", "Real")
                .addOperation(Operation.DIV, "Real", "Real")
                .addOperation(Operation.IDIV, "Integer", "Integer")
                .addOperation(Operation.MOD, "Integer", "Integer")
                .addOperation(Operation.POW, "Integer", "Real")
                .addOperation(Operation.POW, "Real", "Real")
                .addOperation(Operation.SAL, "Integer", "Integer")
                .addOperation(Operation.SAR, "Integer", "Integer")
                .addOperation(Operation.SLR, "Integer", "Integer")
                .addOperation(Operation.AND, "Integer", "Integer")
                .addOperation(Operation.OR, "Integer", "Integer")
                .addOperation(Operation.XOR, "Integer", "Integer")
                .addOperation(Operation.GT, "Integer", "Boolean")
                .addOperation(Operation.GT, "Real", "Boolean")
                .addOperation(Operation.GEQ, "Integer", "Boolean")
                .addOperation(Operation.GEQ, "Real", "Boolean")
                .addOperation(Operation.LT, "Integer", "Boolean")
                .addOperation(Operation.LT, "Real", "Boolean")
                .addOperation(Operation.LEQ, "Integer", "Boolean")
                .addOperation(Operation.LEQ, "Real", "Boolean")
                .create());

        registerType(map, new TypeBuilder("Real")
                .addOperation(Operation.ADD, "Integer", "Real")
                .addOperation(Operation.ADD, "Real", "Real")
                .addOperation(Operation.SUB, "Integer", "Real")
                .addOperation(Operation.SUB, "Real", "Real")
                .addOperation(Operation.MUL, "Integer", "Real")
                .addOperation(Operation.MUL, "Real", "Real")
                .addOperation(Operation.DIV, "Integer", "Real")
                .addOperation(Operation.DIV, "Real", "Real")
                .addOperation(Operation.POW, "Integer", "Real")
                .addOperation(Operation.POW, "Real", "Real")
                .addOperation(Operation.GT, "Integer", "Boolean")
                .addOperation(Operation.GT, "Real", "Boolean")
                .addOperation(Operation.GEQ, "Integer", "Boolean")
                .addOperation(Operation.GEQ, "Real", "Boolean")
                .addOperation(Operation.LT, "Integer", "Boolean")
                .addOperation(Operation.LT, "Real", "Boolean")
                .addOperation(Operation.LEQ, "Integer", "Boolean")
                .addOperation(Operation.LEQ, "Real", "Boolean")
                .addMember(new Member("isFinite", Visibility.PUBLIC, "Function", true))
                .addMember(new Member("isInfinite", Visibility.PUBLIC, "Function", true))
                .addMember(new Member("isNan", Visibility.PUBLIC, "Function", true))
                .addMember(new Member("inf", Visibility.PUBLIC, "Function", true))
                .addMember(new Member("nan", Visibility.PUBLIC, "Function", true))
                .create());

        registerType(map, new TypeBuilder("Boolean")
                .addOperation(Operation.AND, "Boolean", "Boolean")
                .addOperation(Operation.OR, "Boolean", "Boolean")
                .addOperation(Operation.XOR, "Boolean", "Boolean")
                .create());

        registerType(map, new TypeBuilder("Range")
                .setItemAccessible(true)
                .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("begin", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("end", Visibility.PUBLIC, "Function", false))
                .create());

        registerType(map, new TypeBuilder("Array")
                .setItemAccessible(true)
                .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("slice", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("push", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("pop", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("insert", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("remove", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("sort", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("keys", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("values", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("concat", Visibility.PUBLIC, "Function", true))
                .create());

        registerType(map, new TypeBuilder("Dictionary")
                .setItemAccessible(true)
                .addMember(new Member("size", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("has", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("remove", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("keys", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("values", Visibility.PUBLIC, "Function", false))
                .addMember(new Member("merge", Visibility.PUBLIC, "Function", true))
                .create());

        return map;
    }


    private static void registerType(Map<String, Type> typeMap, Type type) {
        typeMap.put(type.getName(), type);
    }
    
}

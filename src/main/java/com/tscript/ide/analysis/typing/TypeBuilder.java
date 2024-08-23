package com.tscript.ide.analysis.typing;

import com.tscript.ide.analysis.utils.Operation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TypeBuilder {
    private final String name;
    private final String displayName;
    private String superType;
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, Map<Operation, String>> operations = new HashMap<>();
    private boolean itemAccessible = false;
    private boolean callable;

    public TypeBuilder(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
        addOperation(Operation.ADD, "String", "String");
    }

    public TypeBuilder(String name){
        this(name, name);
    }

    public TypeBuilder addMember(Member member){
        this.members.put(member.name(), member);
        return this;
    }

    public TypeBuilder addOperation(Operation operation, String with, String returnType){
        Map<Operation, String> lowerOpMap = operations.computeIfAbsent(with, k -> new HashMap<>());
        lowerOpMap.put(operation, returnType);
        return this;
    }

    public TypeBuilder setItemAccessible(boolean accessible){
        this.itemAccessible = accessible;
        return this;
    }

    public TypeBuilder setCallable(boolean flag){
        this.callable = flag;
        return this;
    }

    public TypeBuilder setSuperType(String name){
        this.superType = name;
        return this;
    }

    public Type create(){
        return createType(name, displayName, superType, operations, members, itemAccessible, callable);
    }

    @NotNull
    private static Type createType(String name, String displayName, String superType, Map<String, Map<Operation, String>> operations, Map<String, Member> members, boolean itemAccessible, boolean callable) {
        return new Type() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getPrintName() {
                return displayName;
            }

            @Override
            public Type performOperation(Operation op, Type type, Map<String, Type> typeTable) {
                Map<Operation, String> lowerOpMap = operations.get(type.getName());
                if (lowerOpMap == null)
                    return null;
                String returnType = lowerOpMap.get(op);
                if (returnType == null)
                    return UnknownType.INSTANCE;
                return typeTable.get(returnType);
            }

            @Override
            public Member getMember(String name, Map<String, Type> typeTable) {
                Member member = members.get(name);
                if (member != null && superType == null) return member;
                Type superClass = typeTable.get(superType);
                if (superType == null) return null;
                return superClass.getMember(name, typeTable);
            }

            @Override
            public boolean canItemAccess() {
                return itemAccessible;
            }

            @Override
            public boolean isCallable() {
                return callable;
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }
}

package com.tscript.lang.runtime.tni;

import com.tscript.lang.runtime.tni.types.builtins.*;

import java.util.HashMap;

public class NativeCollection {

    private static final HashMap<String, NativeFunction> collection = new HashMap<>();

    private static void load(NativeFunction callable){
        collection.put(callable.getName(), callable);
    }

    static {
        init();
    }

    private static void init(){
        load(new NativeNull());
        load(new NativeInteger());
        load(new NativeReal());
        load(new NativeString());
        load(new NativeBoolean());
        load(new NativeType());
        load(new NativeFunctionImpl());
        load(new NativeArray());
        load(new NativeDictionary());
        load(new NativeRange());

        load(new NativePrint());
        load(new NativeExit());
    }

    private NativeCollection(){}

    public static NativeFunction load(String name){
        return collection.get(name);
    }

}

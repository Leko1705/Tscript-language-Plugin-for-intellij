package com.test.exec.tscript.runtime.tni;

import com.test.exec.tscript.runtime.type.Callable;

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
        load(new NativePrint());
        load(new NativeExit());
    }

    private NativeCollection(){}

    public static NativeFunction load(String name){
        return collection.get(name);
    }

}

package com.tscript.lang.runtime.tni.util;

import com.tscript.lang.runtime.core.Data;

import java.util.LinkedHashMap;

public class LazyParameterInitializer {

    public static LinkedHashMap<String, Data> create(){
        return new LinkedHashMap<>();
    }

    public static LinkedHashMap<String, Data> create(String name, Data value){
        return new LinkedHashMap<>(){{put(name, value);}};
    }

    public static LinkedHashMap<String, Data> create(String n1, Data v1, String n2, Data v2){
        return new LinkedHashMap<>(){{put(n1, v1);put(n2, v2);}};
    }

    public static LinkedHashMap<String, Data> create(String n1, Data v1, String n2, Data v2, String n3, Data v3){
        LinkedHashMap<String, Data> map = create(n1, v1, n2, v2);
        map.put(n3, v3);
        return map;
    }

}

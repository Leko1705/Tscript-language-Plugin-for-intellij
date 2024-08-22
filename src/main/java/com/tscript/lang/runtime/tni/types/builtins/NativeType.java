package com.tscript.lang.runtime.tni.types.builtins;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;

import java.util.LinkedHashMap;

public class NativeType extends NativeFunction {
    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return caller.unpack(params.get("x")).getType();
    }

    @Override
    public String getName() {
        return "Type";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>(){{put("x", null);}};
    }
}

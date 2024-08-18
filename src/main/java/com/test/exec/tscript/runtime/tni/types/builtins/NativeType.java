package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;

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

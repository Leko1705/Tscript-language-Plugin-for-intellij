package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;
import com.test.exec.tscript.runtime.type.TBoolean;

import java.util.LinkedHashMap;

public class NativeBoolean extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return TBoolean.of(caller.isTrue(params.get("x")));
    }

    @Override
    public String getName() {
        return "Boolean";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>(){{put("x", null);}};
    }
}

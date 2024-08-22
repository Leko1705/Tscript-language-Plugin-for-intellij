package com.tscript.lang.runtime.tni.types.builtins;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.type.TNull;

import java.util.LinkedHashMap;

public class NativeNull extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return TNull.NULL;
    }

    @Override
    public String getName() {
        return "Null";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>();
    }
}

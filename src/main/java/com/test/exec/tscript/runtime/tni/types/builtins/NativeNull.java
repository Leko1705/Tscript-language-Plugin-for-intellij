package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;
import com.test.exec.tscript.runtime.type.TNull;

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

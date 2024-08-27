package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TBoolean;

import java.util.LinkedHashMap;

public class NativeSame extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        Data first = params.get("first");
        Data second = params.get("second");
        return TBoolean.of(first == second);
    }

    @Override
    public String getName() {
        return "same";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("first", null, "second", null);
    }
}

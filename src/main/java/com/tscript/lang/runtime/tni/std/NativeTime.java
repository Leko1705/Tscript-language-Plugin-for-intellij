package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TReal;

import java.util.LinkedHashMap;

public class NativeTime extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return new TReal((double)System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create();
    }

}

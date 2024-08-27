package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TNull;

import java.util.LinkedHashMap;

public class NativeQuitEventMode extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        EventManager.getInstance().quitEventMode(params.get("result"));
        return TNull.NULL;
    }

    @Override
    public String getName() {
        return "quitEventMode";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("result", TNull.NULL);
    }
}

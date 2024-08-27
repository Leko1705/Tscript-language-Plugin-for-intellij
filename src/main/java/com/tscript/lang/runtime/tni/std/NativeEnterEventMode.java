package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;

import java.util.LinkedHashMap;

public class NativeEnterEventMode extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        if (caller.getThreadID() != 0){
            caller.reportRuntimeError("event manager is only runnable from main thread");
            return null;
        }
        return EventManager.getInstance().enterEventMode(caller);
    }

    @Override
    public String getName() {
        return "enterEventMode";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create();
    }
}

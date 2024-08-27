package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TNull;
import com.tscript.lang.runtime.type.TString;

import java.util.LinkedHashMap;

public class NativeAssert extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        Data data = params.get("condition");
        if (!caller.isTrue(data)){
            caller.reportRuntimeError(params.get("message"));
            return null;
        }
        return TNull.NULL;
    }

    @Override
    public String getName() {
        return "assert";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("condition", null, "message", new TString(""));
    }
}

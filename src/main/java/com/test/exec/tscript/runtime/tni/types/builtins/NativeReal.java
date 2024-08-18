package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;
import com.test.exec.tscript.runtime.type.TObject;
import com.test.exec.tscript.runtime.type.TReal;
import com.test.exec.tscript.runtime.type.TType;

import java.util.LinkedHashMap;

public class NativeReal extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        TObject object = caller.unpack(params.get("x"));
        TType type = object.getType();

        if (type == TReal.TYPE)
            return object;

        caller.reportRuntimeError("can not convert " + object + " to Real");
        return null;
    }

    @Override
    public String getName() {
        return "Real";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>(){{put("x", null);}};
    }
}

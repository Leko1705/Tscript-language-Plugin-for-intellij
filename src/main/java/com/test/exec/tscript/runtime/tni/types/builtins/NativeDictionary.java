package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;
import com.test.exec.tscript.runtime.type.TDictionary;
import com.test.exec.tscript.runtime.type.TObject;
import com.test.exec.tscript.runtime.type.TType;

import java.util.LinkedHashMap;

public class NativeDictionary extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        TObject object = caller.unpack(params.get("x"));
        TType type = object.getType();

        if (type == TDictionary.TYPE)
            return new TDictionary(new LinkedHashMap<>(((TDictionary)object).get()));

        caller.reportRuntimeError("can not convert " + object + " to Dictionary");
        return null;
    }

    @Override
    public String getName() {
        return "Dictionary";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>(){{put("x", new TDictionary());}};
    }
}

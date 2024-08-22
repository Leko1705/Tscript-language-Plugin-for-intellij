package com.tscript.lang.runtime.tni.types.builtins;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.type.TArray;
import com.tscript.lang.runtime.type.TObject;
import com.tscript.lang.runtime.type.TType;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class NativeArray extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        TObject object = caller.unpack(params.get("x"));
        TType type = object.getType();

        if (type == TArray.TYPE)
            return new TArray(new ArrayList<>(((TArray)object).get()));

        caller.reportRuntimeError("can not convert " + object + " to Array");
        return null;
    }

    @Override
    public String getName() {
        return "Array";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>(){{put("x", new TArray());}};
    }
}

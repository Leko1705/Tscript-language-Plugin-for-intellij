package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;
import com.test.exec.tscript.runtime.type.TArray;
import com.test.exec.tscript.runtime.type.TObject;
import com.test.exec.tscript.runtime.type.TType;

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

package com.tscript.lang.runtime.tni.types.builtins;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.NativePrint;
import com.tscript.lang.runtime.type.TString;

import java.util.LinkedHashMap;

public class NativeString extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return new TString(NativePrint.makePrintable(caller, params.get("x")));
    }

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>() {{put("x", null);}};
    }
}

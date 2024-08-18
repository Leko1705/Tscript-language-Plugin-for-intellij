package com.test.exec.tscript.runtime.tni.types.builtins;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.tni.NativeFunction;
import com.test.exec.tscript.runtime.tni.NativePrint;
import com.test.exec.tscript.runtime.type.TString;

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

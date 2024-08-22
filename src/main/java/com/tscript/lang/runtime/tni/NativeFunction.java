package com.tscript.lang.runtime.tni;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.type.Callable;

import java.util.LinkedHashMap;

public abstract class NativeFunction extends Callable {

    @Override
    public Data eval(TThread caller, LinkedHashMap<String, Data> params) {
        return run(caller, params);
    }

    public abstract Data run(TThread caller, LinkedHashMap<String, Data> params);

}

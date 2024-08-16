package com.test.exec.tscript.runtime.tni;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;
import com.test.exec.tscript.runtime.type.Callable;

import java.util.LinkedHashMap;

public abstract class NativeFunction extends Callable {

    @Override
    public Data eval(TThread caller, LinkedHashMap<String, Data> params) {
        return run(caller, params);
    }

    public abstract Data run(TThread caller, LinkedHashMap<String, Data> params);

}

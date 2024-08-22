package com.tscript.lang.runtime.tni;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.type.TInteger;
import com.tscript.lang.runtime.type.TNull;
import com.tscript.lang.runtime.type.TObject;

import java.util.LinkedHashMap;

public class NativeExit extends NativeFunction {

    private static final TInteger default_ = new TInteger(0);


    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return new LinkedHashMap<>(){{put("status", default_);}};
    }

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {

        TObject exitCode = caller.unpack(params.get("status"));

        if (!(exitCode instanceof TInteger i)){
            caller.reportRuntimeError("<Integer> is required fo exist code");
            return null;
        }

        System.exit(i.get());
        return TNull.NULL;
    }
}

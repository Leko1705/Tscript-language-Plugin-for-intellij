package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TInteger;
import com.tscript.lang.runtime.type.TNull;

import java.util.LinkedHashMap;

public class NativeWait extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {

        Data data = params.get("ms");
        try {
            Thread.sleep(((TInteger)data).get());
        }
        catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        catch (ClassCastException e){
            caller.reportRuntimeError("<Integer> expected; got: " + data);
            return null;
        }
        return TNull.NULL;
    }

    @Override
    public String getName() {
        return "wait";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("ms", null);
    }

}

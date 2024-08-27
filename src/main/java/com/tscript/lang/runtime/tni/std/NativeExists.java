package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TBoolean;
import com.tscript.lang.runtime.type.TString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

public class NativeExists extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        Data data = params.get("key");
        if (!(data instanceof TString s)){
            caller.reportRuntimeError("string expected; got " + data);
            return null;
        }

        String path = s.get();

        try {
            return TBoolean.of(Files.exists(Path.of(path)));
        }
        catch (SecurityException e){
            caller.reportRuntimeError("can not eval exist() due to missing security permissions");
            return null;
        }
    }

    @Override
    public String getName() {
        return "exists";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("key", null);
    }
}

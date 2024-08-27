package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TDictionary;
import com.tscript.lang.runtime.type.TInteger;
import com.tscript.lang.runtime.type.TNull;
import com.tscript.lang.runtime.type.TString;

import java.util.LinkedHashMap;

public class NativeVersion extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return new TDictionary(getVersionData());
    }

    private static LinkedHashMap<Data, Data> getVersionData(){
        LinkedHashMap<Data, Data> data = new LinkedHashMap<>();

        data.put(new TString("type"), TNull.NULL);
        data.put(new TString("major"), new TInteger(1));
        data.put(new TString("minor"), new TInteger(2));
        data.put(new TString("patch"), new TInteger(2));

        data.put(new TString("day"), new TInteger(0));
        data.put(new TString("month"), new TInteger(0));
        data.put(new TString("year"), new TInteger(2024));
        data.put(new TString("full"), new TString("TScript version 8 - released 0.0.2024 - (C) Lennart Koehler 2024-2024"));

        return data;
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create();
    }

}

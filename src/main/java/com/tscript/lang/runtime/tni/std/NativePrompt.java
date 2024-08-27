package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TNull;
import com.tscript.lang.runtime.type.TString;

import javax.swing.*;
import java.util.LinkedHashMap;

public class NativePrompt extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        String response = JOptionPane.showInputDialog(null, NativePrint.makePrintable(caller, params.get("message")), NativePrint.makePrintable(caller, params.get("default")));
        if (response == null) return TNull.NULL;
        return new TString(response);
    }

    @Override
    public String getName() {
        return "prompt";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("message", null, "default", new TString(""));
    }

}

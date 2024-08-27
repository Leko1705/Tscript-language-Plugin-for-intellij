package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TNull;

import javax.swing.*;
import java.util.LinkedHashMap;

public class NativeAlert extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        JOptionPane.showMessageDialog(null, NativePrint.makePrintable(caller, params.get("message")));
        return TNull.NULL;
    }

    @Override
    public String getName() {
        return "alert";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("message", null);
    }
}

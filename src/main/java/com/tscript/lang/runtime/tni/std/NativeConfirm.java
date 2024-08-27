package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TBoolean;

import javax.swing.*;
import java.util.LinkedHashMap;

public class NativeConfirm extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        int dialogResult = JOptionPane.showConfirmDialog (null, NativePrint.makePrintable(caller, params.get("message")));
        if(dialogResult == JOptionPane.YES_OPTION){
            return TBoolean.TRUE;
        }
        else {
            return TBoolean.FALSE;
        }
    }

    @Override
    public String getName() {
        return "confirm";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("message", null);
    }
}

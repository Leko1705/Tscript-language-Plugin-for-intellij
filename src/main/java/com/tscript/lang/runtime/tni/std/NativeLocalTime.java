package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TReal;

import java.util.Calendar;
import java.util.LinkedHashMap;

public class NativeLocalTime extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        return new TReal((double) localtime());
    }

    @Override
    public String getName() {
        return "localtime";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create();
    }

    private static long localtime() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int offset = calendar.getTimeZone().getOffset(currentTimeMillis);
        return currentTimeMillis + offset;
    }
}

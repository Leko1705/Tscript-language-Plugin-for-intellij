package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.Callable;
import com.tscript.lang.runtime.type.TNull;
import com.tscript.lang.runtime.type.TString;

import java.util.LinkedHashMap;

public class NativeSetEventHandler extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {

        Data event = params.get("event");
        Data handler = params.get("handler");

        if (!(event instanceof TString s)){
            caller.reportRuntimeError("event must be type of String");
            return null;
        }

        if (!(caller.unpack(handler) instanceof Callable callable)){
            caller.reportRuntimeError("handler must be callable");
            return null;
        }

        String eventEncoding = s.get();

        switch (eventEncoding) {
            case "timer" -> EventManager.getInstance().setTimerHandler(callable);
            case "canvas.mousemove" -> EventManager.getInstance().setMouseMoveHandler(callable);
            case "canvas.mousedown" -> EventManager.getInstance().setKeyDownHandler(callable);
            default -> {
                caller.reportRuntimeError("invalid event-type: " + eventEncoding);
                return null;
            }
        }

        return TNull.NULL;
    }

    @Override
    public String getName() {
        return "setEventHandler";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("event", null, "handler", null);
    }

}

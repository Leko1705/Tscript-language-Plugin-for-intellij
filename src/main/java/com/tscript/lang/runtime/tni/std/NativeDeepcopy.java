package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.Callable;
import com.tscript.lang.runtime.type.TArray;
import com.tscript.lang.runtime.type.TDictionary;
import com.tscript.lang.runtime.type.TObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class NativeDeepcopy extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        Data value = params.get("value");

        try {
            return deepCopy(value);
        }
        catch (UnsupportedOperationException e) {
            caller.reportRuntimeError("container must not contain itself");
            return null;
        }
        catch (ClassCastException e) {
            caller.reportRuntimeError("objects are not allowed");
            return null;
        }
        catch (IllegalArgumentException e) {
            caller.reportRuntimeError("callable contents are not allowed");
            return null;
        }
    }

    private TObject deepCopy(Data data){

        if (data.isReference()) throw new ClassCastException();

        TObject value = data.asValue();

        if (value instanceof Callable){
            throw new IllegalArgumentException();
        }

        if (value instanceof TArray a) {
            TArray newArray = new TArray();
            for (Data content : a.get()){
                if (content == a)
                    throw new UnsupportedOperationException();
                newArray.get().add(deepCopy(content));
            }
            return newArray;
        }
        else if (value instanceof TDictionary d) {
            TDictionary newDictionary = new TDictionary();
            for (Map.Entry<Data, Data> entry : d.get().entrySet()){
                if (entry.getKey() == d)
                    throw new UnsupportedOperationException();
                if (entry.getValue() == d)
                    throw new UnsupportedOperationException();
                newDictionary.get().put(deepCopy(entry.getKey()), deepCopy(entry.getValue()));
            }
            return newDictionary;
        }

        return value;
    }

    @Override
    public String getName() {
        return "deepcopy";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("value", null);
    }

}

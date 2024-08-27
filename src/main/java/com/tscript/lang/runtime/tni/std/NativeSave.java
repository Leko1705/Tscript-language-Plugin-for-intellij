package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.tni.NativeFunction;
import com.tscript.lang.runtime.tni.util.LazyParameterInitializer;
import com.tscript.lang.runtime.type.TNull;
import com.tscript.lang.runtime.type.TString;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.LinkedHashMap;

public class NativeSave extends NativeFunction {

    @Override
    public Data run(TThread caller, LinkedHashMap<String, Data> params) {
        Data data = params.get("key");

        if (!(data instanceof TString s)){
            caller.reportRuntimeError("string expected; got " + data);
            return null;
        }

        String path = s.get();

        try(FileWriter fw = new FileWriter(path)) {
            String content =  JSONFormatter.serialize(caller, params.get("value"));
            fw.write(content);
            return TNull.NULL;
        }
        catch (FileNotFoundException e){
            caller.reportRuntimeError("file not found: " + path);
            return null;
        }
        catch (InvalidPathException e){
            caller.reportRuntimeError("invalid path: " + path);
            return null;
        }
        catch (OutOfMemoryError e){
            caller.reportRuntimeError("file is too large");
            return null;
        }
        catch (JSONFormatter.SerializationException e){
            caller.reportRuntimeError(new TString(e.getMessage()));
            return null;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public LinkedHashMap<String, Data> getParameters() {
        return LazyParameterInitializer.create("key", null, "value", null);
    }
}

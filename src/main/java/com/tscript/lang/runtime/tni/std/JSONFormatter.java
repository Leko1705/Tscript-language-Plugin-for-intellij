package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;

class JSONFormatter {

    public static class SerializationException extends Exception {
        public SerializationException(String message) {
            super(message);
        }
    }

    public static String serialize(TThread thread, Data data) throws SerializationException {
        throw new SerializationException("Not supported yet");
    }

    public static Data deserialize(TThread thread, String text) throws SerializationException {
        throw new SerializationException("Not supported yet");
    }

}

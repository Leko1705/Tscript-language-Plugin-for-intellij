package com.tscript.lang.runtime.jit;

import com.tscript.lang.runtime.core.Data;

@JITSensitive
public class GeneratedJITException extends RuntimeException {
    public Data thrown;
    public GeneratedJITException(Data thrown){
        this.thrown = thrown;
    }

}

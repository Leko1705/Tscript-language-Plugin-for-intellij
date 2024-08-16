package com.test.exec.tscript.runtime.jit;

import com.test.exec.tscript.runtime.core.Data;

@JITSensitive
public class GeneratedJITException extends RuntimeException {
    public Data thrown;
    public GeneratedJITException(Data thrown){
        this.thrown = thrown;
    }

}

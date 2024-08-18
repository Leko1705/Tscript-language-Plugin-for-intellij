package com.test.exec.tscript.tscriptc.log;

import com.test.exec.tscript.tscriptc.util.Diagnostics;

public abstract class LoggerAdapter implements Logger {

    public void error(Diagnostics.Error error) { }

    public void warning(Diagnostics.Warning warning) { }
}

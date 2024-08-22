package com.tscript.lang.tscriptc.log;

import com.tscript.lang.tscriptc.util.Diagnostics;

public abstract class LoggerAdapter implements Logger {

    public void error(Diagnostics.Error error) { }

    public void warning(Diagnostics.Warning warning) { }
}

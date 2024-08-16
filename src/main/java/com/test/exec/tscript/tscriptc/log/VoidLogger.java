package com.test.exec.tscript.tscriptc.log;

import com.test.exec.tscript.tscriptc.util.Diagnostics;

public class VoidLogger implements Logger {

    @Override
    public void error(Diagnostics.Error error) { }

    @Override
    public void warning(Diagnostics.Warning warning) { }
}

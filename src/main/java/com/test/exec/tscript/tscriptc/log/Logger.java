package com.test.exec.tscript.tscriptc.log;

import com.test.exec.tscript.tscriptc.util.Diagnostics;

public interface Logger {

    void error(Diagnostics.Error error);

    void warning(Diagnostics.Warning warning);

}

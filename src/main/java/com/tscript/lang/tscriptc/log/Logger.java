package com.tscript.lang.tscriptc.log;

import com.tscript.lang.tscriptc.util.Diagnostics;

public interface Logger {

    void error(Diagnostics.Error error);

    void warning(Diagnostics.Warning warning);

}

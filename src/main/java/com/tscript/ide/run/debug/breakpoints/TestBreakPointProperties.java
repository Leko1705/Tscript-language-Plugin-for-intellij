package com.tscript.ide.run.debug.breakpoints;

import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestBreakPointProperties extends XBreakpointProperties<TestBreakPointProperties> {
    @Override
    public @Nullable TestBreakPointProperties getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TestBreakPointProperties state) {

    }
}

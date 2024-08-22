package com.tscript.ide.run.debug.breakpoints;

import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TscriptBreakPointProperties extends XBreakpointProperties<TscriptBreakPointProperties> {
    @Override
    public @Nullable TscriptBreakPointProperties getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TscriptBreakPointProperties state) {

    }
}

package com.tscript.ide.run.debug;

import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;
import org.jetbrains.annotations.Nullable;

public class TscriptSuspendContext extends XSuspendContext {

    private final XExecutionStack executionStack;

    public TscriptSuspendContext() {
        this.executionStack = new TscriptExecutionStack();
    }

    @Override
    public @Nullable XExecutionStack getActiveExecutionStack() {
        return executionStack;
    }
}


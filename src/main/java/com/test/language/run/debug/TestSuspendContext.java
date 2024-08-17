package com.test.language.run.debug;

import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;
import org.jetbrains.annotations.Nullable;

public class TestSuspendContext extends XSuspendContext {

    private final XExecutionStack executionStack;

    public TestSuspendContext() {
        this.executionStack = new TestExecutionStack();
    }

    @Override
    public @Nullable XExecutionStack getActiveExecutionStack() {
        return executionStack;
    }
}


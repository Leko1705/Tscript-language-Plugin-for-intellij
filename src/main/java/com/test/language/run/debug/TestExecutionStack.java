package com.test.language.run.debug;

import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class TestExecutionStack extends XExecutionStack {

    public TestExecutionStack() {
        super("Main thread");
    }

    @Override
    public @Nullable XStackFrame getTopFrame() {
        // Return the current stack frame (optional)
        return new TestStackFrame();
    }

    @Override
    public void computeStackFrames(int firstFrameIndex, @NotNull XStackFrameContainer container) {
        container.addStackFrames(Collections.singletonList(new TestStackFrame()), true);
    }
}


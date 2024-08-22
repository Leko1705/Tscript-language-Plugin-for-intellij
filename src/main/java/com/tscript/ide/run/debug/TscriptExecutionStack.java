package com.tscript.ide.run.debug;

import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class TscriptExecutionStack extends XExecutionStack {

    public TscriptExecutionStack() {
        super("Main thread");
    }

    @Override
    public @Nullable XStackFrame getTopFrame() {
        // Return the current stack frame (optional)
        return new TscriptStackFrame();
    }

    @Override
    public void computeStackFrames(int firstFrameIndex, @NotNull XStackFrameContainer container) {
        container.addStackFrames(Collections.singletonList(new TscriptStackFrame()), true);
    }
}


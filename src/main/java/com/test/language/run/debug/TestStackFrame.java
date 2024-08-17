package com.test.language.run.debug;

import com.intellij.xdebugger.frame.XStackFrame;
import org.jetbrains.annotations.Nullable;

public class TestStackFrame extends XStackFrame {

    @Nullable
    @Override
    public Object getEqualityObject() {
        return this; // Ensure proper comparison of stack frames
    }
}


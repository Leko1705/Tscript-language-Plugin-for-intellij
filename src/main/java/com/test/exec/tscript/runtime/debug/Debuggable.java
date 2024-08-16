package com.test.exec.tscript.runtime.debug;

import com.test.exec.tscript.runtime.heap.Heap;

public interface Debuggable<I extends DebugInfo> {

    I loadInfo(Heap heap);
    
}

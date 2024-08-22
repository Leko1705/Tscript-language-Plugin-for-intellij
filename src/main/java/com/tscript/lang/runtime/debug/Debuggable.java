package com.tscript.lang.runtime.debug;

import com.tscript.lang.runtime.heap.Heap;

public interface Debuggable<I extends DebugInfo> {

    I loadInfo(Heap heap);
    
}

package com.tscript.lang.runtime.heap.gc;

import com.tscript.lang.runtime.core.Reference;
import com.tscript.lang.runtime.heap.Heap;

import java.util.Collection;

public class EpsilonGC implements GarbageCollector {
    @Override
    public void onAction(int threadID,
                         Heap heap,
                         Reference assigned,
                         Reference displaced,
                         Collection<Reference> roots) {
    }

    @Override
    public GCType getType() {
        return GCType.COUNTING; // for faster start-up
    }
}

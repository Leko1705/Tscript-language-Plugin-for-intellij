package com.tscript.lang.runtime.heap.gc;

import com.tscript.lang.runtime.core.Reference;
import com.tscript.lang.runtime.heap.Heap;

import java.util.Collection;

public class ReferenceCounting implements GarbageCollector {
    @Override
    public void onAction(int threadID,
                         Heap heap,
                         Reference assigned,
                         Reference displaced,
                         Collection<Reference> roots) {
        if (assigned != null)
            assigned.incRC();
        if (displaced != null) {
            displaced.decRC();
            if (displaced.getRC() <= 0)
                heap.free(displaced);
        }
    }

    @Override
    public GCType getType() {
        return GCType.COUNTING;
    }
}

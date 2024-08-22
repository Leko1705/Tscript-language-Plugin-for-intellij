package com.tscript.lang.runtime.heap.gc;

import com.tscript.lang.runtime.core.Reference;
import com.tscript.lang.runtime.heap.Heap;

import java.util.Collection;

public interface GarbageCollector {

    void onAction(int threadID,
                  Heap heap,
                  Reference assigned,
                  Reference displaced,
                  Collection<Reference> roots);

    GCType getType();

}

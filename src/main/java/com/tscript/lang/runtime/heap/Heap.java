package com.tscript.lang.runtime.heap;

import com.tscript.lang.runtime.core.Reference;
import com.tscript.lang.runtime.debug.Debuggable;
import com.tscript.lang.runtime.debug.HeapInfo;
import com.tscript.lang.runtime.type.TObject;

public interface Heap extends Debuggable<HeapInfo> {

    String getName();

    Reference store(TObject object);

    TObject load(Reference ptr);

    int size();

    Reference[] getReferences();

    void free(Reference ptr);

    default void onSurvive(Reference ptr){}

    HeapInfo loadInfo(Heap heap);
}

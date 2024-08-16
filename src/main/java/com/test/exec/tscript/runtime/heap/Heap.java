package com.test.exec.tscript.runtime.heap;

import com.test.exec.tscript.runtime.core.Reference;
import com.test.exec.tscript.runtime.debug.Debuggable;
import com.test.exec.tscript.runtime.debug.HeapInfo;
import com.test.exec.tscript.runtime.type.TObject;

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

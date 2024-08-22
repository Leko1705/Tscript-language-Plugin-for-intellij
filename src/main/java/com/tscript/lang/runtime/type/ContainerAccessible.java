package com.tscript.lang.runtime.type;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;

public interface ContainerAccessible extends TObject {

    Data readFromContainer(TThread thread, Data key);


}

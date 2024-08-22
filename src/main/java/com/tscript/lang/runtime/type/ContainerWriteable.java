package com.tscript.lang.runtime.type;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;

public interface ContainerWriteable extends TObject {

    boolean writeToContainer(TThread thread, Data key, Data value);

}

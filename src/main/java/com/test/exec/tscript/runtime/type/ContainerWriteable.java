package com.test.exec.tscript.runtime.type;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;

public interface ContainerWriteable extends TObject {

    boolean writeToContainer(TThread thread, Data key, Data value);

}

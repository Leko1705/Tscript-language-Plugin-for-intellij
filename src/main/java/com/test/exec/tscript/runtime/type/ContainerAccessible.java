package com.test.exec.tscript.runtime.type;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.core.TThread;

public interface ContainerAccessible extends TObject {

    Data readFromContainer(TThread thread, Data key);


}

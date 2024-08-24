package com.tscript.lang.runtime.debug;

import java.util.List;

public interface FrameInfo extends DebugInfo {

    String getName();

    int getLine();

    List<DataInfo> getStack();

    List<DataInfo> getLocals();

    @Override
    default <P, R> R process(DebugHandler<P, R> handler, P p) {
        return handler.handleFrameInfo(this, p);
    }
}

package com.tscript.lang.runtime.debug;

public interface DebugInfo {

    <P, R> R process(DebugHandler<P, R> handler, P p);

}

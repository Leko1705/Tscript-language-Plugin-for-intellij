package com.test.exec.tscript.runtime.debug;

public interface DebugInfo {

    <P, R> R process(DebugHandler<P, R> handler, P p);

}

package com.test.exec.tscript.tscriptc.analysis;

import com.test.exec.tscript.tscriptc.log.Logger;
import com.test.exec.tscript.tscriptc.tree.RootTree;
import com.test.exec.tscript.tscriptc.util.Diagnostics;
import com.test.exec.tscript.tscriptc.util.TreeScanner;

import java.util.Objects;

public class Checker<P, R> extends TreeScanner<P, R> {

    private Logger log;

    private boolean success = true;

    public boolean check(RootTree rootTree, Logger logger){
        this.log = logger;
        Objects.requireNonNull(rootTree);
        scan(rootTree, null);
        return success;
    }

    public void report(Diagnostics.Error error){
        log.error(error);
        success = false;
    }

}

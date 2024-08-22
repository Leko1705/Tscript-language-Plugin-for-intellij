package com.tscript.lang.tscriptc.analysis;

import com.tscript.lang.tscriptc.log.Logger;
import com.tscript.lang.tscriptc.tree.RootTree;
import com.tscript.lang.tscriptc.util.Diagnostics;
import com.tscript.lang.tscriptc.util.TreeScanner;

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

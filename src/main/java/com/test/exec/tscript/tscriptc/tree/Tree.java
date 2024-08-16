package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.Location;
import com.test.exec.tscript.tscriptc.util.TreeVisitor;

public interface Tree {

    <P, R> R accept(TreeVisitor<P, R> visitor, P p);

    Location getLocation();

    default <P, R> R accept(TreeVisitor<P, R> visitor){
        return accept(visitor, null);
    }

}

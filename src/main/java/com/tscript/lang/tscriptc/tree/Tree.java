package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.Location;
import com.tscript.lang.tscriptc.util.TreeVisitor;

public interface Tree {

    <P, R> R accept(TreeVisitor<P, R> visitor, P p);

    Location getLocation();

    default <P, R> R accept(TreeVisitor<P, R> visitor){
        return accept(visitor, null);
    }

}

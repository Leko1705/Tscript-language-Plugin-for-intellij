package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

public interface ThisTree extends ExpressionTree {

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitThisTree(this, p);
    }
}

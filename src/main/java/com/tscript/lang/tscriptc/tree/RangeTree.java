package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

public interface RangeTree extends ExpressionTree {

    ExpressionTree getFrom();

    ExpressionTree getTo();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitRangeTree(this, p);
    }
}

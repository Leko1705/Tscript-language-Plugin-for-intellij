package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

public interface ClosureTree extends Tree {

    String getName();

    ExpressionTree getExpression();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitClosureTree(this, p);
    }
}

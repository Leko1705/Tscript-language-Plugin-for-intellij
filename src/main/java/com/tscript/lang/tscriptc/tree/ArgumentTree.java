package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

public interface ArgumentTree extends Tree {

    String getReferencedName();

    ExpressionTree getExpression();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitArgumentTree(this, p);
    }
}

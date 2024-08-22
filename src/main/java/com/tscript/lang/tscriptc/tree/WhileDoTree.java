package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

public interface WhileDoTree extends StatementTree {

    ExpressionTree getCondition();

    StatementTree getBody();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitWhileDoTree(this, p);
    }

}

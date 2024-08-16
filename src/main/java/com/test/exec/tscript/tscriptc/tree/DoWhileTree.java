package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

public interface DoWhileTree extends StatementTree {

    StatementTree getBody();

    ExpressionTree getCondition();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitDoWhileTree(this, p);
    }
}

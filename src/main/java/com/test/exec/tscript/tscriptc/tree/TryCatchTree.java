package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

public interface
TryCatchTree extends StatementTree {

    StatementTree getTryBody();

    String getExceptionName();

    StatementTree getCatchBody();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitTryCatchTree(this, p);
    }
}

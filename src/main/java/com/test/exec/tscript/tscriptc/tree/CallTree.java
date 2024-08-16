package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

import java.util.List;

public interface CallTree extends UnaryExpressionTree, UsefulExpression {

    List<ArgumentTree> getArguments();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitCallTree(this, p);
    }
}

package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

import java.util.List;

public interface BlockTree extends StatementTree {

    List<StatementTree> getStatements();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitBlockTree(this, p);
    }
}

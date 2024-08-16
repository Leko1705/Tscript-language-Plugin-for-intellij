package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

public interface ImportTree extends StatementTree {

    String[] getPath();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitImportTree(this, p);
    }
}

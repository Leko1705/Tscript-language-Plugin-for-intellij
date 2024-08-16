package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

import java.util.List;

public interface RootTree extends Tree {

    List<DefinitionTree> getDefinitions();

    List<StatementTree> getStatements();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitRootTree(this, p);
    }
}

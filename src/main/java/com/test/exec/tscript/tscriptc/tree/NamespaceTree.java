package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

import java.util.List;

public interface NamespaceTree extends DefinitionTree {


    String getName();

    List<DefinitionTree> getDefinitions();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitNamespaceTree(this, p);
    }
}

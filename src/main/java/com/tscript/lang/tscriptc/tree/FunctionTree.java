package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

import java.util.List;

public interface FunctionTree extends CallableTree {

    String getName();

    List<ParameterTree> getParameters();

    BlockTree getBody();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitFunctionTree(this, p);
    }
}

package com.test.exec.tscript.tscriptc.tree;

import com.test.exec.tscript.tscriptc.util.TreeVisitor;

import java.util.List;

public interface DictionaryTree extends ExpressionTree {

    List<ExpressionTree> getKeys();

    List<ExpressionTree> getValues();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitDictionaryTree(this, p);
    }
}

package com.tscript.lang.tscriptc.tree;

import com.tscript.lang.tscriptc.util.TreeVisitor;

import java.util.List;

public interface DictionaryTree extends ExpressionTree {

    List<ExpressionTree> getKeys();

    List<ExpressionTree> getValues();

    @Override
    default <P, R> R accept(TreeVisitor<P, R> visitor, P p) {
        return visitor.visitDictionaryTree(this, p);
    }
}

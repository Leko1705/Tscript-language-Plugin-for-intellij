package com.tscript.lang.tscriptc.tree;

@InheritOnly
public interface BinaryExpressionTree extends ExpressionTree {

    ExpressionTree getLeft();

    ExpressionTree getRight();

}

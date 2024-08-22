package com.tscript.lang.tscriptc.tree;

@InheritOnly
public interface LiteralTree<T> extends ExpressionTree {

    T get();

}

package com.tscript.lang.tscriptc.tree;

import java.util.List;

@InheritOnly
public interface CallableTree extends DefinitionTree {

    List<ParameterTree> getParameters();

}

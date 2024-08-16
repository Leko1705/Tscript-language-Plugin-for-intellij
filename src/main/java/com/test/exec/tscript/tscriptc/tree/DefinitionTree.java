package com.test.exec.tscript.tscriptc.tree;

import java.util.Set;

@InheritOnly
public interface DefinitionTree extends Tree {

    Set<Modifier> getModifiers();

    String getName();

}

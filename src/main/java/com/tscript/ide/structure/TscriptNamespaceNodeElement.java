package com.tscript.ide.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.tscript.ide.psi.TestNamespaceDef;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TscriptNamespaceNodeElement extends NavTreeNodeElement<TestNamespaceDef> {

    public TscriptNamespaceNodeElement(TestNamespaceDef element) {
        super(element);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        return resolveAllForChildren(myElement);
    }
}

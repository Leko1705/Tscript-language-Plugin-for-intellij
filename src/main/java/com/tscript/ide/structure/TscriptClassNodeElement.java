package com.tscript.ide.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.tscript.ide.psi.TestClassDef;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class TscriptClassNodeElement extends NavTreeNodeElement<TestClassDef> {

    public TscriptClassNodeElement(TestClassDef element) {
        super(element);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        if (myElement.getClassBodyDef() == null)
            return List.of();
        return resolveAllForChildren(myElement.getClassBodyDef());
    }

}

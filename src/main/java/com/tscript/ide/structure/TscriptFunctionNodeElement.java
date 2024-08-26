package com.tscript.ide.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.tscript.ide.psi.TestFunctionDef;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class TscriptFunctionNodeElement extends NavTreeNodeElement<TestFunctionDef> {

    public TscriptFunctionNodeElement(TestFunctionDef element) {
        super(element);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        return List.of();
    }
}

package com.tscript.ide.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TscriptFileNodeElement extends NavTreeNodeElement<PsiFile> {

    public TscriptFileNodeElement(PsiFile element) {
        super(element);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        return resolveAllForChildren(myElement);
    }

}
package com.tscript.ide.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.tscript.ide.psi.TestClassDef;
import com.tscript.ide.psi.TestFunctionDef;
import com.tscript.ide.psi.TestNamespaceDef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TscriptStructureViewModel
        extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {

    public TscriptStructureViewModel(@Nullable Editor editor, PsiFile psiFile) {
        super(psiFile, editor, new TscriptFileNodeElement(psiFile));
    }

    @NotNull
    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return false;
    }

    @Override
    protected Class<?> @NotNull [] getSuitableClasses() {
        return new Class[]{TestNamespaceDef.class, TestClassDef.class, TestFunctionDef.class};
    }

}

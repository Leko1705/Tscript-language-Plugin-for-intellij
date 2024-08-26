package com.tscript.ide.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class NavTreeNodeElement<E extends NavigatablePsiElement>
    extends PsiTreeElementBase<E>
        implements StructureViewTreeElement, SortableTreeElement {


    public static Collection<StructureViewTreeElement> resolveAllForChildren(PsiElement element){
        List<StructureViewTreeElement> elements = new ArrayList<>();

        element.acceptChildren(new TestVisitor(){

            @Override
            public void visitDefinition(@NotNull TestDefinition o) {
                o.acceptChildren(this);
            }

            @Override
            public void visitClassDef(@NotNull TestClassDef o) {
                elements.add(new TscriptClassNodeElement(o));
            }

            @Override
            public void visitFunctionDef(@NotNull TestFunctionDef o) {
                elements.add(new TscriptFunctionNodeElement(o));
            }

            @Override
            public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                elements.add(new TscriptNamespaceNodeElement(o));
            }
        });

        return elements;
    }


    public final E myElement;

    public NavTreeNodeElement(E element) {
        super(element);
        this.myElement = element;
    }

    @Override
    public E getValue() {
        return myElement;
    }

    @Override
    public void navigate(boolean requestFocus) {
        myElement.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return myElement.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return myElement.canNavigateToSource();
    }

    @Override
    public @NotNull String getAlphaSortKey() {
        return (myElement.getName() == null) ? "" : myElement.getName();
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        ItemPresentation presentation = myElement.getPresentation();
        return presentation != null ? presentation : new PresentationData();
    }

    @Override
    public @NlsSafe @Nullable String getPresentableText() {
        return getPresentation().getPresentableText();
    }

    @Override
    public abstract @NotNull Collection<StructureViewTreeElement> getChildrenBase();


}

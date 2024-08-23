package com.tscript.ide.structure;

import com.intellij.icons.AllIcons;
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension;
import com.intellij.lang.Language;
import com.intellij.psi.PsiFile;
import com.tscript.ide.TscriptLanguage;
import com.tscript.ide.psi.TestClassDef;
import com.tscript.ide.psi.TestFunctionDef;
import com.tscript.ide.psi.TestNamespaceDef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TscriptAwareNavBar extends StructureAwareNavBarModelExtension {
    @NotNull
    @Override
    protected Language getLanguage() {
        return TscriptLanguage.INSTANCE;
    }

    @Override
    public @Nullable String getPresentableText(Object object) {

        if (object instanceof PsiFile file) {
            return file.getName();
        }

        if (object instanceof TestClassDef def){
            return def.getName();
        }

        if (object instanceof TestFunctionDef def){
            return def.getName();
        }

        if (object instanceof TestNamespaceDef def){
            return def.getName();
        }

        return null;
    }

    @Override
    public @Nullable Icon getIcon(Object object) {

        if (object instanceof TestClassDef){
            return AllIcons.Nodes.Class;
        }

        if (object instanceof TestFunctionDef){
            return AllIcons.Nodes.Function;
        }

        if (object instanceof TestNamespaceDef){
            return AllIcons.Nodes.Package;
        }

        return null;
    }
}

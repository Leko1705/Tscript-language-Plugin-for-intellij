package com.tscript.ide.psi;

import com.intellij.psi.tree.IElementType;
import com.tscript.ide.TscriptLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TestElementType extends IElementType {

    public TestElementType(@NotNull @NonNls String debugName) {
        super(debugName, TscriptLanguage.INSTANCE);
    }

}

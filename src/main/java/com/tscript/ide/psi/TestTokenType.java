package com.tscript.ide.psi;

import com.intellij.psi.tree.IElementType;
import com.tscript.ide.TestLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TestTokenType extends IElementType {

    public TestTokenType(@NotNull @NonNls String debugName) {
        super(debugName, TestLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "TestTokenType." + super.toString();
    }
}

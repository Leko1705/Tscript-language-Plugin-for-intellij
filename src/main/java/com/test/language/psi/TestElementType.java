package com.test.language.psi;

import com.intellij.psi.tree.IElementType;
import com.test.language.TestLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TestElementType extends IElementType {

    public TestElementType(@NotNull @NonNls String debugName) {
        super(debugName, TestLanguage.INSTANCE);
    }

}

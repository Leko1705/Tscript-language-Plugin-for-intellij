package com.tscript.ide.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.tscript.ide.TscriptFileType;
import com.tscript.ide.TscriptLanguage;
import org.jetbrains.annotations.NotNull;

public class TestFile extends PsiFileBase {

    public TestFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TscriptLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return TscriptFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Simple File";
    }

}
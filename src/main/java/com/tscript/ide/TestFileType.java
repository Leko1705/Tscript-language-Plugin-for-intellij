package com.tscript.ide;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TestFileType extends LanguageFileType {

    public static final TestFileType INSTANCE = new TestFileType();

    private TestFileType() {
        super(TestLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Test File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Test language file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "test";
    }

    @Override
    public Icon getIcon() {
        return TestIcon.FILE;
    }
}

package com.tscript.ide;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TscriptFileType extends LanguageFileType {

    public static final TscriptFileType INSTANCE = new TscriptFileType();

    private TscriptFileType() {
        super(TscriptLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Tscript File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Tscript language file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "tscript";
    }

    @Override
    public Icon getIcon() {
        return TscriptIcon.FILE;
    }
}

package com.tscript.ide.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tscript.ide.run.debug.TscriptDebugState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TscriptRunConfiguration extends RunConfigurationBase<TscriptRunConfigurationOptions> {

    VirtualFile file;

    protected TscriptRunConfiguration(Project project,
                                      ConfigurationFactory factory,
                                      String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected TscriptRunConfigurationOptions getOptions() {
        return (TscriptRunConfigurationOptions) super.getOptions();
    }

    public String getScriptName() {
        return getOptions().getScriptName();
    }

    public void setScriptName(String scriptName) {
        getOptions().setScriptName(scriptName);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new TscriptRunSettingsEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

        FileEditorManager fileEditorManager = FileEditorManager.getInstance(getProject());

        VirtualFile[] files = fileEditorManager.getOpenFiles();


        if (files.length == 0 || (file = fileEditorManager.getOpenFiles()[0]) == null) {
            throw new RuntimeConfigurationError("No runnable tscript file open");
        }
    }


    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {

        if (executor instanceof DefaultRunExecutor) {
            return new TscriptRunState(environment, executor, file);
        }
        else if (executor instanceof DefaultDebugExecutor) {
            return new TscriptDebugState(environment, executor, file);
        }

        return null;
    }

}

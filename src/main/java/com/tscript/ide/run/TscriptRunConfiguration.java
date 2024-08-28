package com.tscript.ide.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.tscript.ide.TscriptIcon;
import com.tscript.ide.run.build.BuildTscriptTask;
import com.tscript.ide.run.debug.IntellijDebugger;
import com.tscript.ide.run.debug.TscriptDebugProcess;
import com.tscript.ide.run.debug.TscriptDebugState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.debugger.DebuggableRunConfiguration;

import javax.swing.*;
import java.net.InetSocketAddress;

public class TscriptRunConfiguration
        extends RunConfigurationBase<TscriptRunConfigurationOptions>
        implements RunProfileWithCompileBeforeLaunchOption, DebuggableRunConfiguration {

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
    public @Nullable Icon getIcon() {
        return TscriptIcon.FILE;
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {

        if (executor instanceof DefaultRunExecutor) {
            return new TscriptRunState(environment, executor, getScriptName());
        }
        else if (executor instanceof DefaultDebugExecutor) {
            return new TscriptDebugState(environment, executor, getScriptName());
        }

        return null;
    }

    @Override
    public @NotNull XDebugProcess createDebugProcess(@NotNull InetSocketAddress socketAddress, @NotNull XDebugSession session, @Nullable ExecutionResult executionResult, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        VirtualFile file = getCompiledFile(environment, getScriptName());
        return new TscriptDebugProcess(
                session,
                new TscriptProcessHandler(file.getPath(), new IntellijDebugger(session, file)));
    }


    public static @NotNull VirtualFile getCompiledFile(ExecutionEnvironment environment, String scriptFileName) throws ExecutionException {

        VirtualFile file;

        if (scriptFileName.isEmpty()){
            DataContext context = environment.getDataContext();
            if (context == null) {
                throwFileNotFound(scriptFileName);
            }
            // fixme: ProfileState is built before build-process -> incorrect file-not-found-error
            file = context.getData(CommonDataKeys.VIRTUAL_FILE);
        }
        else {
            file = LocalFileSystem.getInstance().findFileByPath(scriptFileName);
        }

        if (file == null){
            throwFileNotFound(scriptFileName);
        }

        scriptFileName = file.getPath();

        if (!BuildTscriptTask.compiledFiles.containsKey(scriptFileName)){
            throwCompiledNotFound(file.getPath());
        }

        file = LocalFileSystem.getInstance().findFileByPath(BuildTscriptTask.compiledFiles.get(scriptFileName));

        if (file == null){
            throwCompiledNotFound(scriptFileName);
        }

        return file;
    }

    private static void throwFileNotFound(String file) throws ExecutionException {
        throw new ExecutionException("File not found: " + file);
    }

    private static void throwCompiledNotFound(String file) throws ExecutionException {
        throw new ExecutionException("Compiled file not found for: " + file);
    }

}

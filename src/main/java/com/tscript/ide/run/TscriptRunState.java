package com.tscript.ide.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tscript.ide.run.util.RunUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TscriptRunState extends CommandLineState {

    private final Executor executor;
    private final VirtualFile file;
    
    protected TscriptRunState(ExecutionEnvironment environment, Executor executor, VirtualFile file) {
        super(environment);
        this.executor = executor;
        this.file = file;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        Project project = getEnvironment().getProject();

        TscriptProcessHandler handler = RunUtils.createProcessHandler(
                Objects.requireNonNull(createConsole(executor)),
                () -> new TscriptProcessHandler(project, file.getPath()));

        new Thread(handler).start();
        return handler;
    }

}

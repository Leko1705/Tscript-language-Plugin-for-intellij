package com.tscript.ide.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.vfs.VirtualFile;
import com.tscript.ide.run.util.RunUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TscriptRunState extends CommandLineState {

    private final String scriptFileName;
    private final Executor executor;

    protected TscriptRunState(ExecutionEnvironment environment, Executor executor, String scriptFileName) {
        super(environment);
        this.scriptFileName = scriptFileName;
        this.executor = executor;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {

        VirtualFile compiledFile = TscriptRunConfiguration.getCompiledFile(getEnvironment(), scriptFileName);

        TscriptProcessHandler handler = RunUtils.createProcessHandler(
                Objects.requireNonNull(createConsole(executor)),
                () -> new TscriptProcessHandler(compiledFile.getPath()));

        new Thread(handler).start();
        return handler;
    }

}

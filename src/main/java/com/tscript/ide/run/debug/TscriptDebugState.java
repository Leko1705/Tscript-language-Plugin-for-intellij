package com.tscript.ide.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.tscript.ide.run.TscriptProcessHandler;
import com.tscript.ide.run.TscriptRunConfiguration;
import com.tscript.ide.run.util.RunUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class TscriptDebugState extends CommandLineState {

    private final String scriptFileName;
    private final Executor executor;

    public TscriptDebugState(ExecutionEnvironment environment, Executor executor, @NotNull String scriptFileName) {
        super(environment);
        this.executor = executor;
        this.scriptFileName = scriptFileName;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        ConsoleView console = Objects.requireNonNull(createConsole(executor));
        return RunUtils.createProcessHandler(console, this::getProcessHandler);
    }

    private ProcessHandler getProcessHandler() throws ExecutionException {
        Project project = getEnvironment().getProject();

        VirtualFile scriptFile = LocalFileSystem.getInstance().findFileByIoFile(new File(scriptFileName));
        if (scriptFile == null) {
            throw new ExecutionException("Cannot find script file: " + scriptFileName);
        }
        VirtualFile compiledFile = TscriptRunConfiguration.getCompiledFile(getEnvironment(), scriptFileName);

        try {
            XDebugSession session = XDebuggerManager.getInstance(project).startSession(
                    getEnvironment(),
                    new XDebugProcessStarter() {
                        @Override
                        public @NotNull XDebugProcess start(@NotNull XDebugSession session) {
                            return new TscriptDebugProcess(
                                    session,
                                    new TscriptProcessHandler(compiledFile.getPath(), new IntellijDebugger(session, scriptFile)));
                        }
                    }
            );

            return session.getDebugProcess().getProcessHandler();
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}

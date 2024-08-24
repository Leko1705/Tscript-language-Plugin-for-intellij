package com.tscript.ide.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.tscript.ide.run.TscriptProcessHandler;
import com.tscript.ide.run.util.RunUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TscriptDebugState extends CommandLineState {

    private final VirtualFile file;
    private final Executor executor;

    public TscriptDebugState(ExecutionEnvironment environment, Executor executor, VirtualFile file) {
        super(environment);
        this.executor = executor;
        this.file = file;
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        ConsoleView console = Objects.requireNonNull(createConsole(executor));
        return RunUtils.createProcessHandler(console, this::getProcessHandler);
    }

    private ProcessHandler getProcessHandler() {
        Project project = getEnvironment().getProject();

        try {
            XDebugSession session = XDebuggerManager.getInstance(project).startSession(
                    getEnvironment(),
                    new XDebugProcessStarter() {
                        @Override
                        public @NotNull XDebugProcess start(@NotNull XDebugSession session) {
                            return new TscriptDebugProcess(
                                    session,
                                    new TscriptProcessHandler(project, file.getPath(), new IntellijDebugger(session, file)));
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

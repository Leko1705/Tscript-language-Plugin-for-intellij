package com.tscript.ide.run.debug;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.tscript.ide.TscriptFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TscriptDebugProcess extends XDebugProcess {

    private final ProcessHandler handler;

    public TscriptDebugProcess(@NotNull XDebugSession session, ProcessHandler handler) {
        super(session);
        this.handler = handler;
    }

    @Override
    protected @Nullable ProcessHandler doGetProcessHandler() {
        return handler;
    }

    @Override
    public @NotNull XDebuggerEditorsProvider getEditorsProvider() {
        return new XDebuggerEditorsProvider() {
            @Override
            public @NotNull FileType getFileType() {
                return TscriptFileType.INSTANCE;
            }
        };
    }

    @Override
    public void startStepOver(@Nullable XSuspendContext context) {
        super.startStepOver(context);
    }

    @Override
    public void startStepInto(@Nullable XSuspendContext context) {
        super.startStepInto(context);
    }

    @Override
    public void startStepOut(@Nullable XSuspendContext context) {
        super.startStepOut(context);
    }

    @Override
    public void stop() {
        // Implement logic to stop the debugging process
    }

    @Override
    public void resume(@Nullable XSuspendContext context) {
        super.resume(context);
    }

    @Override
    public void startPausing() {
        // Implement logic to pause the execution
    }

}


package com.tscript.ide.run.debug;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.tscript.ide.TscriptFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TscriptDebugProcess extends XDebugProcess {

    private final ProcessHandler processHandler;

    public TscriptDebugProcess(@NotNull XDebugSession session, ProcessHandler processHandler) {
        super(session);
        this.processHandler = processHandler;
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
    protected @Nullable ProcessHandler doGetProcessHandler() {
        return processHandler;
    }
}

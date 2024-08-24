package com.tscript.ide.run;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.tscript.ide.run.util.RunUtils;
import com.tscript.lang.runtime.core.FDEListener;
import com.tscript.lang.runtime.core.TscriptVM;
import com.tscript.lang.runtime.debug.Debugger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class TscriptProcessHandler extends ProcessHandler implements Runnable {

    private final Project project;
    private final String path;
    private final PrintStream prevOut, prevErr;
    private final Debugger debugger;
    private volatile boolean running = true;

    public TscriptProcessHandler(Project project, String path) {
        this(project, path, null);
    }

    public TscriptProcessHandler(Project project, String path, Debugger debugger) {
        this.project = project;
        this.path = path;
        this.prevOut = System.out;
        this.prevErr = System.err;
        this.debugger = debugger;
    }

    @Override
    protected void destroyProcessImpl() {
        onTermination();
        notifyProcessTerminated(0);
    }

    @Override
    protected void detachProcessImpl() {
        onTermination();
        notifyProcessDetached();
    }

    @Override
    public boolean detachIsDefault() {
        return true;
    }

    @Override
    public @Nullable OutputStream getProcessInput() {
        return null;
    }

    @Override
    public void run() {
        RunUtils.compile(project, path, (exitCode, compiledPath) -> {
            if (exitCode != 0) {
                onTermination();
                notifyProcessTerminated(exitCode);
                return;
            }

            doExecute(compiledPath);
        });
    }

    private void doExecute(String compiled) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        toolWindowManager.invokeLater(() -> toolWindowManager.getToolWindow("Run").activate(null));

        running = true;
        int exitCode = exec(compiled);
        System.out.println("\nExit Code: " + exitCode);
        notifyProcessTerminated(exitCode);
        onTermination();
    }

    private void onTermination(){
        System.setOut(prevOut);
        System.setErr(prevErr);
        running = false;
    }


    private int exec(String path){
        try {
            TscriptVM VM = TscriptVM.build(new File(path), System.out, System.err, debugger);
            VM.attatchFDEListener(new InterruptListener());
            return VM.execute();
        }
        catch (ProcessCanceledException e){
            return -1;
        }
    }

    private class InterruptListener implements FDEListener {
        @Override
        public void onAction() {
            if (!running)
                throw new ProcessCanceledException();
        }
    }

}

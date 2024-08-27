package com.tscript.ide.run;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.tscript.lang.runtime.core.FDEListener;
import com.tscript.lang.runtime.core.TscriptVM;
import com.tscript.lang.runtime.debug.Debugger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class TscriptProcessHandler extends ProcessHandler implements Runnable {

    private final String path;
    private final PrintStream prevOut, prevErr;
    private final Debugger debugger;
    private volatile boolean running = true;

    public TscriptProcessHandler(String compiledPath) {
        this(compiledPath, null);
    }

    public TscriptProcessHandler(String compiledPath, Debugger debugger) {
        this.path = compiledPath;
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
        doExecute(path);
    }

    private void doExecute(String compiled) {
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

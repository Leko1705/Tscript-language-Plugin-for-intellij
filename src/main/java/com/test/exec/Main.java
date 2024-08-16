package com.test.exec;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.test.exec.tscript.runtime.core.FDEListener;
import com.test.exec.tscript.runtime.core.TscriptVM;
import com.test.exec.tscript.tscriptc.log.Logger;
import com.test.exec.tscript.tscriptc.tools.Compiler;
import com.test.exec.tscript.tscriptc.tools.CompilerProvider;
import com.test.exec.tscript.tscriptc.util.Diagnostics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Main {

    public static void run(@NotNull String filePath, @Nullable ProgressIndicator indicator) {
        if (indicator != null) {
            indicator.setFraction(0);
        }
        String compiledPath = compile(filePath);
        if (compiledPath == null) return;
        if (indicator != null) {
            indicator.setFraction(1);
        }
        exec(compiledPath, indicator);
    }

    private static String compile(String path, String... args){
        try (InputStream in = new FileInputStream(path)){
            Compiler compiler = CompilerProvider.getDefaultTscriptCompiler();

            OutputStream out = new FileOutputStream(path + "c");
            compiler.run(in, out, new IDELogger(), args);
            return path + "c";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (ProcessCanceledException e){
            return null;
        }
    }

    private static void exec(String path, @Nullable ProgressIndicator indicator){
        long start = System.currentTimeMillis();

        TscriptVM VM = TscriptVM.build(new File(path), System.out, System.err);
        if (indicator != null)
            VM.attatchFDEListener(new InterruptListener(indicator));
        int exitValue = VM.execute();

        long end = System.currentTimeMillis();
        System.out.println("\nExit Value: " + exitValue + " (exec time: " + (end - start) + "ms)");
    }


    private static class IDELogger implements Logger {

        @Override
        public void error(Diagnostics.Error error) {
            System.err.println(error.toString());
            throw new ProcessCanceledException();
        }

        @Override
        public void warning(Diagnostics.Warning warning) {
            // warnings already highlighted in IDE
        }
    }


    private record InterruptListener(ProgressIndicator indicator) implements FDEListener {

            private InterruptListener(@NotNull ProgressIndicator indicator) {
                this.indicator = indicator;
            }

            @Override
            public void onAction() {
                indicator.checkCanceled();
            }
        }

}

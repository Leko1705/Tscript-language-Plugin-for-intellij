package com.test.language.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.test.exec.tscript.runtime.core.FDEListener;
import com.test.exec.tscript.runtime.core.TscriptVM;
import com.test.exec.tscript.tscriptc.log.Logger;
import com.test.exec.tscript.tscriptc.tools.Compiler;
import com.test.exec.tscript.tscriptc.tools.CompilerProvider;
import com.test.exec.tscript.tscriptc.util.Diagnostics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;

public class TestRunConfiguration extends RunConfigurationBase<TestRunConfigurationOptions> {

    protected TestRunConfiguration(Project project,
                                   ConfigurationFactory factory,
                                   String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected TestRunConfigurationOptions getOptions() {
        return (TestRunConfigurationOptions) super.getOptions();
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
        return new TestRunSettingsEditor();
    }


    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {

        DataContext context = environment.getDataContext();
        if (context == null){
            Messages.showErrorDialog("Can not find script file " + getScriptName(), "File Not Found");
            return null;
        }

        VirtualFile file = context.getData(CommonDataKeys.VIRTUAL_FILE);
        if (file == null) {
            Messages.showErrorDialog("Can not find script file " + getScriptName(), "File Not Found");
            return null;
        }

        String path = file.getPath();

        return new CommandLineState(environment) {

            @Override
            protected @NotNull ProcessHandler startProcess() throws ExecutionException {

                ConsoleView consoleView = createConsole(executor);
                Objects.requireNonNull(consoleView);

                PrintStream prevOut = System.out;
                PrintStream prevErr = System.err;

                TscriptProcessHandler handler = new TscriptProcessHandler(path, prevOut, prevErr);
                consoleView.attachToProcess(handler);

                System.setOut(new ConsoleOutputStream(prevOut, handler, ProcessOutputTypes.STDOUT));
                System.setErr(new ConsoleOutputStream(prevErr, handler, ProcessOutputTypes.STDERR));

                new Thread(handler).start();
                return handler;
            }
        };

    }




    private static class TscriptProcessHandler extends ProcessHandler implements Runnable {

        private final String path;
        private final PrintStream prevOut, prevErr;
        private volatile boolean running = true;

        public TscriptProcessHandler(String path, PrintStream prevOut, PrintStream prevErr) {
            this.path = path;
            this.prevOut = prevOut;
            this.prevErr = prevErr;
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
            String compiledPath = compile(path);
            if (compiledPath == null){
                notifyProcessTerminated(0);
                return;
            }

            running = true;
            int exitCode = exec(compiledPath);
            System.out.println("\nExit Code: " + exitCode);
            notifyProcessTerminated(exitCode);
            onTermination();
        }

        private void onTermination(){
            System.setOut(prevOut);
            System.setErr(prevErr);
            running = false;
        }

        private String compile(String path, String... args){
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

        private int exec(String path){
            try {
                TscriptVM VM = TscriptVM.build(new File(path), System.out, System.err);
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


    private static class ConsoleOutputStream extends PrintStream {

        private final ProcessHandler handler;
        private final Key<?> outputType;

        public ConsoleOutputStream(@NotNull OutputStream out, ProcessHandler handler, Key<?> outputType) {
            super(out);
            this.handler = handler;
            this.outputType = outputType;
        }

        @Override
        public void print(@Nullable String s) {
            super.print(s);
            handler.notifyTextAvailable(Objects.toString(s), outputType);
        }

        @Override
        public void println(@Nullable String x) {
            String s = x + '\n';
            super.print(s);
            handler.notifyTextAvailable(s, outputType);
        }

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



}

package com.test.language.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.task.ProjectTaskManager;
import com.test.exec.tscript.runtime.core.FDEListener;
import com.test.exec.tscript.runtime.core.TscriptVM;
import com.test.language.run.build.BuildTscriptTask;
import org.jetbrains.concurrency.Promise;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

        FileEditorManager fileEditorManager = FileEditorManager.getInstance(environment.getProject());
        VirtualFile currentFile = fileEditorManager.getOpenFiles()[0];
        if (currentFile == null) {
            Messages.showErrorDialog("No file is currently open", "No File Open");
            return null;

        }

        String path = currentFile.getPath();

        return new CommandLineState(environment) {

            @Override
            protected @NotNull ProcessHandler startProcess() throws ExecutionException {

                ConsoleView consoleView = createConsole(executor);
                Objects.requireNonNull(consoleView);

                PrintStream prevOut = System.out;
                PrintStream prevErr = System.err;

                TscriptProcessHandler handler = new TscriptProcessHandler(environment.getProject(), path, prevOut, prevErr);
                consoleView.attachToProcess(handler);

                System.setOut(new ConsoleOutputStream(prevOut, handler, ProcessOutputTypes.STDOUT));
                System.setErr(new ConsoleOutputStream(prevErr, handler, ProcessOutputTypes.STDERR));

                new Thread(handler).start();
                return handler;
            }
        };

    }




    private static class TscriptProcessHandler extends ProcessHandler implements Runnable {

        private final Project project;
        private final String path;
        private final PrintStream prevOut, prevErr;
        private volatile boolean running = true;

        public TscriptProcessHandler(Project project, String path, PrintStream prevOut, PrintStream prevErr) {
            this.project = project;
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

            String compiled = BuildTscriptTask.cached.get(path);

            if (compiled == null || !Files.exists(Path.of(compiled))){
                Promise<ProjectTaskManager.Result> promise = ProjectTaskManager.getInstance(project).buildAllModules();
                promise.onProcessed(o -> {
                    if (o.hasErrors() || o.isAborted()) {
                        onTermination();
                        notifyProcessTerminated(-1);
                        return;
                    }

                    String recompiled = BuildTscriptTask.cached.get(path);

                    if (recompiled == null || !Files.exists(Path.of(recompiled))){
                        EventQueue.invokeLater(() -> Messages.showErrorDialog("Can not compile file " + path, "File Not Found"));
                        onTermination();
                        notifyProcessTerminated(0);
                        return;
                    }

                    doExecute(recompiled);
                });
                return;
            }

           doExecute(compiled);
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





}

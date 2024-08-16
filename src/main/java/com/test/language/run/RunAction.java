package com.test.language.run;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.test.exec.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

public class RunAction extends AnAction {

    private static final String CONSOLE_ID = "Run";
    private static final String DEFAULT_CONSOLE_NAME = "Console";

    private ConsoleView consoleView;
    private ToolWindow toolWindow;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE);

        if (selectedFile == null) {
            return;
        }
        // Your logic here, for example:
        String filePath = selectedFile.getPath();

        Project project = e.getProject();
        if (project == null) {
            return; // Exit if no project is open
        }

        if (consoleView == null) {
            consoleView = new ConsoleViewImpl(project, true);

            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            toolWindow = toolWindowManager.getToolWindow(CONSOLE_ID);
            if (toolWindow == null) {
                toolWindow = toolWindowManager.registerToolWindow(CONSOLE_ID, true, ToolWindowAnchor.BOTTOM);
            }

            Content content = ContentFactory.getInstance().createContent(consoleView.getComponent(), DEFAULT_CONSOLE_NAME, false);
            toolWindow.getContentManager().addContent(content);
        }

        consoleView.clear();
        toolWindow.show();

        PrintStream prevOut = System.out;
        System.setOut(new ConsoleOutputStream(System.out, consoleView, ConsoleViewContentType.NORMAL_OUTPUT));

        PrintStream prevErr = System.err;
        System.setErr(new ConsoleOutputStream(System.err, consoleView, ConsoleViewContentType.ERROR_OUTPUT));


        // Start the background task
        new Task.Backgroundable(project, "Executing Tscript script", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                Main.run(filePath, indicator);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                System.setOut(prevOut);
                System.setErr(prevErr);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                System.setOut(prevOut);
                System.setErr(prevErr);
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                System.setOut(prevOut);
                System.setErr(prevErr);
            }

            @Override
            public void onThrowable(@NotNull Throwable error) {
                super.onThrowable(error);
                System.setOut(prevOut);
                System.setErr(prevErr);
            }
        }.queue();

    }


    private static class ConsoleOutputStream extends PrintStream implements ConsoleView {

        private final ConsoleView view;
        private final ConsoleViewContentType printType;

        public ConsoleOutputStream(@NotNull OutputStream out, ConsoleView view, ConsoleViewContentType printType) {
            super(out);
            this.view = view;
            this.printType = printType;
        }

        @Override
        public void print(@Nullable String s) {
            super.print(s);
            print(Objects.toString(s), printType);
        }

        @Override
        public void println(@Nullable String x) {
            String s = x + '\n';
            super.print(s);
            print(s, printType);
        }

        @Override
        public void print(@NotNull String text, @NotNull ConsoleViewContentType contentType) {
            view.print(text, contentType);
        }

        @Override
        public void clear() {
            view.clear();
        }

        @Override
        public void scrollTo(int offset) {
            view.scrollTo(offset);
        }

        @Override
        public void attachToProcess(@NotNull ProcessHandler processHandler) {
            view.attachToProcess(processHandler);
        }

        @Override
        public void setOutputPaused(boolean value) {
            view.setOutputPaused(value);
        }

        @Override
        public boolean isOutputPaused() {
            return view.isOutputPaused();
        }

        @Override
        public boolean hasDeferredOutput() {
            return view.hasDeferredOutput();
        }

        @Override
        public void performWhenNoDeferredOutput(@NotNull Runnable runnable) {
            view.performWhenNoDeferredOutput(runnable);
        }

        @Override
        public void setHelpId(@NotNull String helpId) {
            view.setHelpId(helpId);
        }

        @Override
        public void addMessageFilter(@NotNull Filter filter) {
            view.addMessageFilter(filter);
        }

        @Override
        public void printHyperlink(@NotNull String hyperlinkText, @Nullable HyperlinkInfo info) {
            view.printHyperlink(hyperlinkText, info);
            view.scrollTo(view.getContentSize());
        }

        @Override
        public int getContentSize() {
            return view.getContentSize();
        }

        @Override
        public boolean canPause() {
            return view.canPause();
        }

        @Override
        public AnAction @NotNull [] createConsoleActions() {
            return view.createConsoleActions();
        }

        @Override
        public void allowHeavyFilters() {
            view.allowHeavyFilters();
        }

        @Override
        public @NotNull JComponent getComponent() {
            return view.getComponent();
        }

        @Override
        public JComponent getPreferredFocusableComponent() {
            return view.getPreferredFocusableComponent();
        }

        @Override
        public void dispose() {
            view.dispose();
        }

    }

}

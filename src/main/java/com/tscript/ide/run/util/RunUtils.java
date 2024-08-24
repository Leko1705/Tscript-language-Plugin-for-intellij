package com.tscript.ide.run.util;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.task.ProjectTaskManager;
import com.tscript.ide.run.TscriptRunState;
import com.tscript.ide.run.build.BuildTscriptTask;
import org.apache.tools.ant.types.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.Promise;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RunUtils {

    public static <Handler extends ProcessHandler> Handler createProcessHandler(@NotNull ConsoleView consoleView, Supplier<Handler> handlerProvider) {
        Objects.requireNonNull(consoleView);

        Handler handler = handlerProvider.get();
        consoleView.attachToProcess(handler);

        System.setOut(new ConsoleOutputStream(System.out, handler, ProcessOutputTypes.STDOUT));
        System.setErr(new ConsoleOutputStream(System.err, handler, ProcessOutputTypes.STDERR));

        return handler;
    }

    public static void compile(Project project, String path, BiConsumer<Integer, String> callback){
        String compiled = BuildTscriptTask.cached.get(path);

        if (compiled == null || !Files.exists(Path.of(compiled))){
            Promise<ProjectTaskManager.Result> promise = ProjectTaskManager.getInstance(project).buildAllModules();
            promise.onProcessed(o -> {
                if (o.hasErrors() || o.isAborted()) {
                    callback.accept(-1, null);
                    return;
                }

                String recompiled = BuildTscriptTask.cached.get(path);

                if (recompiled == null || !Files.exists(Path.of(recompiled))){
                    EventQueue.invokeLater(() -> Messages.showErrorDialog("Can not compile file " + path, "File Not Found"));
                    callback.accept(-1, null);
                    return;
                }

                callback.accept(0, recompiled);
            });
            return;
        }

        callback.accept(0, compiled);
    }

}

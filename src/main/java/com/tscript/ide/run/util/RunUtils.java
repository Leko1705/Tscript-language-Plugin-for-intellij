package com.tscript.ide.run.util;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.ui.ConsoleView;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class RunUtils {

    public static <Handler extends ProcessHandler> Handler createProcessHandler(@NotNull ConsoleView consoleView,
                                                                                Supplier<Handler> handlerProvider) {

        Objects.requireNonNull(consoleView);

        Handler handler = handlerProvider.get();
        consoleView.attachToProcess(handler);

        System.setOut(new ConsoleOutputStream(System.out, handler, ProcessOutputTypes.STDOUT));
        System.setErr(new ConsoleOutputStream(System.err, handler, ProcessOutputTypes.STDERR));

        return handler;
    }

}

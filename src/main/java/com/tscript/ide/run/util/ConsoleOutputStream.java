package com.tscript.ide.run.util;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

public class ConsoleOutputStream extends PrintStream {

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

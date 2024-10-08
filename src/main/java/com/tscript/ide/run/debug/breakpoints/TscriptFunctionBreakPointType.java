package com.tscript.ide.run.debug.breakpoints;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.tscript.ide.TscriptFileType;
import com.tscript.ide.psi.TestConstructorDef;
import com.tscript.ide.psi.TestFunctionDef;
import com.tscript.ide.psi.TestLambdaExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TscriptFunctionBreakPointType extends XLineBreakpointType<TscriptBreakPointProperties> {

    protected TscriptFunctionBreakPointType() {
        super("TestFunctionBreakPoint", "NoTitle");
    }

    @Override
    public @Nullable TscriptBreakPointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return new TscriptBreakPointProperties();
    }

    @Override
    public @Nullable TscriptBreakPointProperties createProperties() {
        return new TscriptBreakPointProperties();
    }

    @Override
    public @NotNull Icon getEnabledIcon() {
        return AllIcons.Debugger.Db_method_breakpoint;
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        if (!FileTypeRegistry.getInstance().isFileOfType(file, TscriptFileType.INSTANCE)) return false;
        return LineIterator.canPutAtElement(
                file,
                line,
                project,
                (element, document) -> element instanceof TestFunctionDef
                        || element instanceof TestConstructorDef
                        || element instanceof TestLambdaExpr);
    }

}


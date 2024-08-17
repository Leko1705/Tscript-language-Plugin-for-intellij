package com.test.language.run.debug.breakpoints;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.test.language.TestFileType;
import com.test.language.psi.TestConstructorDef;
import com.test.language.psi.TestFunctionDef;
import com.test.language.psi.TestLambdaExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TestFunctionBreakPointType extends XLineBreakpointType<TestBreakPointProperties> {

    protected TestFunctionBreakPointType() {
        super("TestFunctionBreakPoint", "NoTitle");
    }

    @Override
    public @Nullable TestBreakPointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return new TestBreakPointProperties();
    }

    @Override
    public @Nullable TestBreakPointProperties createProperties() {
        return new TestBreakPointProperties();
    }

    @Override
    public @NotNull Icon getEnabledIcon() {
        return AllIcons.Debugger.Db_method_breakpoint;
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        if (!FileTypeRegistry.getInstance().isFileOfType(file, TestFileType.INSTANCE)) return false;
        return LineIterator.canPutAtElement(
                file,
                line,
                project,
                (element, document) -> element instanceof TestFunctionDef
                        || element instanceof TestConstructorDef
                        || element instanceof TestLambdaExpr);
    }

}


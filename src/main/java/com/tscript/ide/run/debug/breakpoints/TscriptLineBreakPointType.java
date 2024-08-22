package com.tscript.ide.run.debug.breakpoints;

import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.tscript.ide.TscriptFileType;
import com.tscript.ide.psi.TestStmt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TscriptLineBreakPointType extends XLineBreakpointType<TscriptBreakPointProperties> {

    protected TscriptLineBreakPointType() {
        super("TestLineBreakPoint", "NoTitle");
    }

    @Override
    public @Nullable TscriptBreakPointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return new TscriptBreakPointProperties();
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        if (!FileTypeRegistry.getInstance().isFileOfType(file, TscriptFileType.INSTANCE)) return false;
        return LineIterator.canPutAtElement(
                file,
                line,
                project,
                (element, document) -> element instanceof TestStmt);
    }

}

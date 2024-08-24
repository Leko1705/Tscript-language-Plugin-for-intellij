package com.tscript.ide.run.debug;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.*;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.tscript.lang.runtime.debug.Debugger;
import com.tscript.lang.runtime.debug.FrameInfo;
import com.tscript.lang.runtime.debug.ThreadInfo;
import com.tscript.lang.runtime.debug.VMInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntellijDebugger extends Debugger {

    private final VirtualFile file;
    private XDebugSession session;

    public IntellijDebugger(XDebugSession session, @NotNull VirtualFile file) {
        this.session = session;
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void onDebug(int threadID, VMInfo info) {
        XSuspendContext state = new DebugPauseState(info, threadID, file);
        session.positionReached(state);
    }



    private static class DebugPauseState extends XSuspendContext {

        private XExecutionStack currentStack;
        private final XExecutionStack[] allStacks;

        public DebugPauseState(VMInfo info, int threadID, VirtualFile file) {
            List<XExecutionStack> stacks = new ArrayList<>();

            for (ThreadInfo threadInfo : info.getThreadTrees()){
                XExecutionStack stack = new ThreadState(threadInfo, file);

                if (threadInfo.getID() == threadID) {
                    assertCurrentStackNotSet();
                    currentStack = stack;
                }

                stacks.add(stack);
            }
            Objects.requireNonNull(currentStack);
            allStacks = stacks.toArray(new XExecutionStack[0]);
        }


        private void assertCurrentStackNotSet(){
            if (currentStack != null)
                throw new AssertionError("multiple current threads running with the same ID");
        }


        @Override
        public XExecutionStack @NotNull [] getExecutionStacks() {
            return allStacks;
        }

        @Override
        public @Nullable XExecutionStack getActiveExecutionStack() {
            return currentStack;
        }

    }



    private static class ThreadState extends XExecutionStack {

        private final ThreadInfo info;
        private final VirtualFile file;

        protected ThreadState(ThreadInfo info, VirtualFile file) {
            super("Thread_" + info.getID());
            this.info = info;
            this.file = file;
        }

        @Override
        public @Nullable XStackFrame getTopFrame() {
            return new FrameState(info.getFrameTrees().get(0), file);
        }

        @Override
        public void computeStackFrames(int firstFrameIndex, XStackFrameContainer container) {
            List<XStackFrame> frames = new ArrayList<>();
            for (FrameInfo f : info.getFrameTrees()) {
                frames.add(new FrameState(f, file));
            }
            container.addStackFrames(frames, true);
        }
    }


    private static class FrameState extends XStackFrame {

        private final FrameInfo frameInfo;
        private final VirtualFile file;

        private FrameState(FrameInfo info, VirtualFile file) {
            this.frameInfo = info;
            this.file = file;
        }

        @Override
        public @Nullable Object getEqualityObject() {
            return frameInfo.getLine();
        }

        @Override
        public @Nullable XSourcePosition getSourcePosition() {
            return XDebuggerUtil.getInstance().createPosition(file, frameInfo.getLine());
        }

    }

}

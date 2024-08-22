package com.tscript.lang.runtime.jit;

import java.util.Deque;
import java.util.LinkedList;

public class UnsafeReducer extends ParentDelegationPhase<Void> {

    private record LastLine(BytecodeParser.NewLineTree lineTree, BytecodeParser.Tree parent){
    }

    private final Deque<LastLine> lastLineStack = new LinkedList<>();

    private LastLine lastLine = null;

    private boolean unsafeDetected = false;


    private Void handleUnsafe(BytecodeParser.Tree tree){
        scan(tree, tree);
        lastLine = null; // hold the line alive
        unsafeDetected = true;
        return null;
    }

    private void enterScope(){
        lastLineStack.push(lastLine);
        lastLine = null;
    }

    private void leaveScope(){
        lastLine = lastLineStack.pop();
    }

    private void removeLastLine(){
        lastLine.parent.remove(lastLine.lineTree);
        lastLine = null;
        optimizationPerformed();
    }

    @Override
    public Void visitRootTree(BytecodeParser.RootTree rootTree, BytecodeParser.Tree parent) {
        super.visitRootTree(rootTree, parent);
        if(lastLine != null)
            removeLastLine();
        return null;
    }

    @Override
    public Void visitNewLineTree(BytecodeParser.NewLineTree newLineTree, BytecodeParser.Tree parent) {
        if (lastLine != null) {
            removeLastLine();
            return null;
        }
        lastLine = new LastLine(newLineTree, parent);
        return null;
    }

    @Override
    public Void visitTryCatchTree(BytecodeParser.TryCatchTree tryCatchTree, BytecodeParser.Tree parent) {
        boolean outerUnsafeDetected = unsafeDetected;
        unsafeDetected = false;

        enterScope();
        scan(tryCatchTree.tryBody, tryCatchTree);
        leaveScope();

        if (!unsafeDetected){
            parent.replace(tryCatchTree, tryCatchTree.tryBody);
            unsafeDetected = outerUnsafeDetected;
            return null;
        }

        unsafeDetected = outerUnsafeDetected;
        scan(tryCatchTree.catchBody, tryCatchTree);
        return null;
    }

    @Override
    public Void visitIfElseTree(BytecodeParser.IfElseTree ifElseTree, BytecodeParser.Tree parent) {
        scan(ifElseTree.condition, ifElseTree);

        enterScope();
        scan(ifElseTree.ifBody, ifElseTree);
        leaveScope();

        enterScope();
        scan(ifElseTree.elseBody, ifElseTree);
        leaveScope();

        return null;
    }

    @Override
    public Void visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, BytecodeParser.Tree parent) {
        scan(forLoopTree.iterable, forLoopTree);

        enterScope();
        scan(forLoopTree.body, forLoopTree);
        leaveScope();

        return null;
    }

    @Override
    public Void visitLoadMemberTree(BytecodeParser.LoadMemberTree loadTree, BytecodeParser.Tree parent) {
        return handleUnsafe(loadTree.exp);
    }

    @Override
    public Void visitReadContainerTree(BytecodeParser.ReadContainerTree readTree, BytecodeParser.Tree parent) {
        return handleUnsafe(readTree);
    }

    @Override
    public Void visitStoreMemberTree(BytecodeParser.StoreMemberTree storeTree, BytecodeParser.Tree parent) {
        return handleUnsafe(storeTree);
    }

    @Override
    public Void visitWriteContainerTree(BytecodeParser.WriteContainerTree writeTree, BytecodeParser.Tree parent) {
        return handleUnsafe(writeTree);
    }

    @Override
    public Void visitRangeTree(BytecodeParser.RangeTree rangeTree, BytecodeParser.Tree parent) {
        scan(rangeTree.to, rangeTree);
        scan(rangeTree.from, rangeTree);
        lastLine = null; // hold the line alive
        unsafeDetected = true;
        return null;
    }

    @Override
    public Void visitThrowTree(BytecodeParser.ThrowTree throwTree, BytecodeParser.Tree parent) {
        return handleUnsafe(throwTree);
    }

    @Override
    public Void visitCallTree(BytecodeParser.CallTree callTree, BytecodeParser.Tree parent) {
        return handleUnsafe(callTree);
    }

    @Override
    public Void visitLoadAbstractImplTree(BytecodeParser.LoadAbstractImplTree loadTree, BytecodeParser.Tree parent) {
        return handleUnsafe(null);
    }

    @Override
    public Void visitConstantTree(BytecodeParser.ConstantTree constantTree, BytecodeParser.Tree parent) {
        return handleUnsafe(null);
    }

    @Override
    public Void visitCallSuperTree(BytecodeParser.CallSuperTree callSuperTree, BytecodeParser.Tree parent) {
        return handleUnsafe(callSuperTree);
    }

}

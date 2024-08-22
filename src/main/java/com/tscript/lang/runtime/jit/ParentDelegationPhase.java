package com.tscript.lang.runtime.jit;

public abstract class ParentDelegationPhase<R> extends OptimizationPhase<R, BytecodeParser.Tree> {

    public ParentDelegationPhase(){
    }
    
    public ParentDelegationPhase(R defaultValue){
        super(defaultValue);
    }
    
    @Override
    public R visitRangeTree(BytecodeParser.RangeTree rangeTree, BytecodeParser.Tree parent) {
        return super.visitRangeTree(rangeTree, rangeTree);
    }

    @Override
    public R visitGetTypeTree(BytecodeParser.GetTypeTree getTypeTree, BytecodeParser.Tree parent) {
        return super.visitGetTypeTree(getTypeTree, getTypeTree);
    }

    @Override
    public R visitThrowTree(BytecodeParser.ThrowTree throwTree, BytecodeParser.Tree parent) {
        return super.visitThrowTree(throwTree, throwTree);
    }

    @Override
    public R visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, BytecodeParser.Tree parent) {
        return super.visitForLoopTree(forLoopTree, forLoopTree);
    }

    @Override
    public R visitIfElseTree(BytecodeParser.IfElseTree ifElseTree, BytecodeParser.Tree parent) {
        return super.visitIfElseTree(ifElseTree, ifElseTree);
    }

    @Override
    public R visitArgumentTree(BytecodeParser.ArgumentTree argumentTree, BytecodeParser.Tree parent) {
        return super.visitArgumentTree(argumentTree, argumentTree);
    }

    @Override
    public R visitBinaryOperationTree(BytecodeParser.BinaryOperationTree operationTree, BytecodeParser.Tree parent) {
        return super.visitBinaryOperationTree(operationTree, operationTree);
    }

    @Override
    public R visitReturnTree(BytecodeParser.ReturnTree returnTree, BytecodeParser.Tree parent) {
        return super.visitReturnTree(returnTree, returnTree);
    }

    @Override
    public R visitUnaryOperationTree(BytecodeParser.UnaryOperationTree operationTree, BytecodeParser.Tree parent) {
        return super.visitUnaryOperationTree(operationTree, operationTree);
    }

    @Override
    public R visitWriteContainerTree(BytecodeParser.WriteContainerTree writeTree, BytecodeParser.Tree parent) {
        return super.visitWriteContainerTree(writeTree, writeTree);
    }

    @Override
    public R visitCallTree(BytecodeParser.CallTree callTree, BytecodeParser.Tree parent) {
        return super.visitCallTree(callTree, callTree);
    }

    @Override
    public R visitBooleanTree(BytecodeParser.BooleanTree booleanTree, BytecodeParser.Tree parent) {
        return super.visitBooleanTree(booleanTree, booleanTree);
    }

    @Override
    public R visitIntegerTree(BytecodeParser.IntegerTree integerTree, BytecodeParser.Tree parent) {
        return super.visitIntegerTree(integerTree, integerTree);
    }

    @Override
    public R visitStringTree(BytecodeParser.StringTree stringTree, BytecodeParser.Tree tree) {
        return super.visitStringTree(stringTree, stringTree);
    }

    @Override
    public R visitTryCatchTree(BytecodeParser.TryCatchTree tryCatchTree, BytecodeParser.Tree parent) {
        return super.visitTryCatchTree(tryCatchTree, tryCatchTree);
    }

    @Override
    public R visitSequenceTree(BytecodeParser.SequenceTree sequenceTree, BytecodeParser.Tree parent) {
        return super.visitSequenceTree(sequenceTree, sequenceTree);
    }

    @Override
    public R visitRootTree(BytecodeParser.RootTree rootTree, BytecodeParser.Tree parent) {
        return super.visitRootTree(rootTree, rootTree);
    }

    @Override
    public R visitJavaCodeTree(BytecodeParser.JavaCodeTree javaCodeTree, BytecodeParser.Tree parent) {
        return super.visitJavaCodeTree(javaCodeTree, javaCodeTree);
    }

    @Override
    public R visitDictionaryTree(BytecodeParser.DictionaryTree dictionaryTree, BytecodeParser.Tree parent) {
        return super.visitDictionaryTree(dictionaryTree, dictionaryTree);
    }

    @Override
    public R visitThisTree(BytecodeParser.ThisTree thisTree, BytecodeParser.Tree parent) {
        return super.visitThisTree(thisTree, thisTree);
    }

    @Override
    public R visitArrayTree(BytecodeParser.ArrayTree arrayTree, BytecodeParser.Tree parent) {
        return super.visitArrayTree(arrayTree, arrayTree);
    }

    @Override
    public R visitCallSuperTree(BytecodeParser.CallSuperTree callSuperTree, BytecodeParser.Tree parent) {
        return super.visitCallSuperTree(callSuperTree, callSuperTree);
    }

    @Override
    public R visitConstantTree(BytecodeParser.ConstantTree constantTree, BytecodeParser.Tree parent) {
        return super.visitConstantTree(constantTree, constantTree);
    }

    @Override
    public R visitEqualsTree(BytecodeParser.EqualsTree equalsTree, BytecodeParser.Tree parent) {
        return super.visitEqualsTree(equalsTree, equalsTree);
    }

    @Override
    public R visitLoadAbstractImplTree(BytecodeParser.LoadAbstractImplTree loadTree, BytecodeParser.Tree parent) {
        return super.visitLoadAbstractImplTree(loadTree, loadTree);
    }

    @Override
    public R visitLoadGlobalTree(BytecodeParser.LoadGlobalTree loadGlobalTree, BytecodeParser.Tree parent) {
        return super.visitLoadGlobalTree(loadGlobalTree, loadGlobalTree);
    }

    @Override
    public R visitLoadLocalTree(BytecodeParser.LoadLocalTree loadLocalTree, BytecodeParser.Tree parent) {
        return super.visitLoadLocalTree(loadLocalTree, loadLocalTree);
    }

    @Override
    public R visitLoadMemberFastTree(BytecodeParser.LoadMemberFastTree loadTree, BytecodeParser.Tree parent) {
        return super.visitLoadMemberFastTree(loadTree, loadTree);
    }

    @Override
    public R visitLoadMemberTree(BytecodeParser.LoadMemberTree loadTree, BytecodeParser.Tree parent) {
        return super.visitLoadMemberTree(loadTree, loadTree);
    }

    @Override
    public R visitLoadStaticTree(BytecodeParser.LoadStaticTree loadTree, BytecodeParser.Tree parent) {
        return super.visitLoadStaticTree(loadTree, loadTree);
    }

    @Override
    public R visitNewLineTree(BytecodeParser.NewLineTree newLineTree, BytecodeParser.Tree parent) {
        return super.visitNewLineTree(newLineTree, newLineTree);
    }

    @Override
    public R visitNullTree(BytecodeParser.NullTree nullTree, BytecodeParser.Tree parent) {
        return super.visitNullTree(nullTree, nullTree);
    }

    @Override
    public R visitReadContainerTree(BytecodeParser.ReadContainerTree readTree, BytecodeParser.Tree parent) {
        return super.visitReadContainerTree(readTree, readTree);
    }

    @Override
    public R visitStoreGlobalTree(BytecodeParser.StoreGlobalTree storeGlobalTree, BytecodeParser.Tree parent) {
        return super.visitStoreGlobalTree(storeGlobalTree, storeGlobalTree);
    }

    @Override
    public R visitStoreLocalTree(BytecodeParser.StoreLocalTree storeLocalTree, BytecodeParser.Tree parent) {
        return super.visitStoreLocalTree(storeLocalTree, storeLocalTree);
    }

    @Override
    public R visitStoreMemberFastTree(BytecodeParser.StoreMemberFastTree storeTree, BytecodeParser.Tree parent) {
        return super.visitStoreMemberFastTree(storeTree, storeTree);
    }

    @Override
    public R visitStoreMemberTree(BytecodeParser.StoreMemberTree storeTree, BytecodeParser.Tree parent) {
        return super.visitStoreMemberTree(storeTree, storeTree);
    }

    @Override
    public R visitStoreStaticTree(BytecodeParser.StoreStaticTree storeTree, BytecodeParser.Tree parent) {
        return super.visitStoreStaticTree(storeTree, storeTree);
    }
}

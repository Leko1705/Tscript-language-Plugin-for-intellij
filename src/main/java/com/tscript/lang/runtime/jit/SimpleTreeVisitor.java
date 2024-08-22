package com.tscript.lang.runtime.jit;

import java.util.List;

public class SimpleTreeVisitor<R, P> implements TreeVisitor<R, P> {
    
    private final R defaultValue;

    public SimpleTreeVisitor(){
        defaultValue = null;
    }
    
    public SimpleTreeVisitor(R defaultValue){
        this.defaultValue = defaultValue;
    }

    public R getDefaultValue() {
        return defaultValue;
    }

    public R selectResult(R r1, R r2){
        return r1;
    }

    public R scan(BytecodeParser.Tree tree, P p){
        return (tree != null) ? tree.accept(this, p) : defaultValue;
    }

    public R scan(BytecodeParser.Tree tree){
        return scan(tree, null);
    }

    public R scan(List<? extends BytecodeParser.Tree> trees, P p){
        R r = defaultValue;
        for (BytecodeParser.Tree tree : trees)
            r = scanSelective(tree, p, r);
        return r;
    }

    public R scanSelective(BytecodeParser.Tree tree, P p, R r){
        return selectResult(scan(tree, p), r);
    }

    public R scanSelective(List<? extends BytecodeParser.Tree> trees, P p, R r){
        for (BytecodeParser.Tree tree : trees)
            r = scanSelective(tree, p, r);
        return r;
    }
    @Override
    public R visitRootTree(BytecodeParser.RootTree rootTree, P p) {
        return scan(rootTree.tree, p);
    }

    @Override
    public R visitSequenceTree(BytecodeParser.SequenceTree sequenceTree, P p) {
        return scan(sequenceTree.children, p);
    }

    @Override
    public R visitReturnTree(BytecodeParser.ReturnTree returnTree, P p) {
        return scan(returnTree.expression, p);
    }

    @Override
    public R visitNullTree(BytecodeParser.NullTree nullTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitIntegerTree(BytecodeParser.IntegerTree integerTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitRealTree(BytecodeParser.RealTree realTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitBooleanTree(BytecodeParser.BooleanTree booleanTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitStringTree(BytecodeParser.StringTree stringTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitConstantTree(BytecodeParser.ConstantTree constantTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitLoadLocalTree(BytecodeParser.LoadLocalTree loadLocalTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitStoreLocalTree(BytecodeParser.StoreLocalTree storeLocalTree, P p) {
        return scan(storeLocalTree.child, p);
    }

    @Override
    public R visitLoadGlobalTree(BytecodeParser.LoadGlobalTree loadGlobalTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitStoreGlobalTree(BytecodeParser.StoreGlobalTree storeGlobalTree, P p) {
        return scan(storeGlobalTree.child, p);
    }

    @Override
    public R visitBinaryOperationTree(BytecodeParser.BinaryOperationTree operationTree, P p) {
        R r = scan(operationTree.left, p);
        r = scanSelective(operationTree.right, p, r);
        return r;
    }

    @Override
    public R visitUnaryOperationTree(BytecodeParser.UnaryOperationTree operationTree, P p) {
        return scan(operationTree.exp, p);
    }

    @Override
    public R visitThisTree(BytecodeParser.ThisTree thisTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitEqualsTree(BytecodeParser.EqualsTree equalsTree, P p) {
        R r = scan(equalsTree.left, p);
        r = scanSelective(equalsTree.right, p, r);
        return r;
    }

    @Override
    public R visitCallTree(BytecodeParser.CallTree callTree, P p) {
        R r = scan(callTree.called, p);
        r = scanSelective(callTree.arguments, p, r);
        return r;
    }

    @Override
    public R visitCallSuperTree(BytecodeParser.CallSuperTree callSuperTree, P p) {
        return scan(callSuperTree.arguments(), p);
    }

    @Override
    public R visitArgumentTree(BytecodeParser.ArgumentTree argumentTree, P p) {
        return scan(argumentTree.exp, p);
    }

    @Override
    public R visitGetTypeTree(BytecodeParser.GetTypeTree getTypeTree, P p) {
        return scan(getTypeTree.exp, p);
    }

    @Override
    public R visitThrowTree(BytecodeParser.ThrowTree throwTree, P p) {
        return scan(throwTree.exp, p);
    }

    @Override
    public R visitArrayTree(BytecodeParser.ArrayTree arrayTree, P p) {
        return scan(arrayTree.arguments(), p);
    }

    @Override
    public R visitDictionaryTree(BytecodeParser.DictionaryTree dictionaryTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitRangeTree(BytecodeParser.RangeTree rangeTree, P p) {
        R r = scan(rangeTree.from, p);
        r = scanSelective(rangeTree.to, p, r);
        return r;
    }

    @Override
    public R visitNewLineTree(BytecodeParser.NewLineTree newLineTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitLoadMemberFastTree(BytecodeParser.LoadMemberFastTree loadTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitStoreMemberFastTree(BytecodeParser.StoreMemberFastTree storeTree, P p) {
        return scan(storeTree.child, p);
    }

    @Override
    public R visitLoadMemberTree(BytecodeParser.LoadMemberTree loadTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitStoreMemberTree(BytecodeParser.StoreMemberTree storeTree, P p) {
        return scan(storeTree.child, p);
    }

    @Override
    public R visitAccessUnknownFastTree(BytecodeParser.AccessUnknownFastTree accessTre, P p) {
        return defaultValue;
    }

    @Override
    public R visitLoadStaticTree(BytecodeParser.LoadStaticTree loadTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitStoreStaticTree(BytecodeParser.StoreStaticTree storeTree, P p) {
        return scan(storeTree.child, p);
    }

    @Override
    public R visitWriteContainerTree(BytecodeParser.WriteContainerTree writeTree, P p) {
        R r = scan(writeTree.container, p);
        r = scanSelective(writeTree.key, p, r);
        r = scanSelective(writeTree.exp, p, r);
        return r;
    }

    @Override
    public R visitReadContainerTree(BytecodeParser.ReadContainerTree readTree, P p) {
        R r = scan(readTree.container, p);
        r = scanSelective(readTree.key, p, r);
        return r;
    }

    @Override
    public R visitLoadAbstractImplTree(BytecodeParser.LoadAbstractImplTree loadTree, P p) {
        return defaultValue;
    }

    @Override
    public R visitTryCatchTree(BytecodeParser.TryCatchTree tryCatchTree, P p) {
        R r = scan(tryCatchTree.tryBody, p);
        r = scanSelective(tryCatchTree.catchBody, p, r);
        return r;
    }

    @Override
    public R visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, P p) {
        R r = scan(forLoopTree.iterable, p);
        r = scanSelective(forLoopTree.body, p, r);
        return r;
    }

    @Override
    public R visitIfElseTree(BytecodeParser.IfElseTree ifElseTree, P p) {
        R r = scan(ifElseTree.condition, p);
        r = scanSelective(ifElseTree.ifBody, p, r);
        r = scanSelective(ifElseTree.elseBody, p, r);
        return r;
    }

    @Override
    public R visitJavaCodeTree(BytecodeParser.JavaCodeTree javaCodeTree, P p) {
        return defaultValue;
    }
}

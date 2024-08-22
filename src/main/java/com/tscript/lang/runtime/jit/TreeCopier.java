package com.tscript.lang.runtime.jit;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class TreeCopier extends SimpleTreeVisitor<BytecodeParser.Tree, Void> {

    private static final TreeCopier copier = new TreeCopier();

    public static BytecodeParser.Tree copy(BytecodeParser.Tree tree){
        return tree.accept(copier);
    }

    private TreeCopier(){
    }

    @Override
    public BytecodeParser.Tree visitRootTree(BytecodeParser.RootTree rootTree, Void unused) {
        return new BytecodeParser.RootTree(scan(rootTree.tree));
    }

    @Override
    public BytecodeParser.Tree visitSequenceTree(BytecodeParser.SequenceTree sequenceTree, Void unused) {
        BytecodeParser.SequenceTree sequence = new BytecodeParser.SequenceTree();
        for (BytecodeParser.Tree child : sequenceTree.children)
            sequence.children.add(scan(child));
        return sequence;
    }

    @Override
    public BytecodeParser.Tree visitReturnTree(BytecodeParser.ReturnTree returnTree, Void unused) {
        return new BytecodeParser.ReturnTree(scan(returnTree.expression));
    }

    @Override
    public BytecodeParser.Tree visitNullTree(BytecodeParser.NullTree nullTree, Void unused) {
        return new BytecodeParser.NullTree();
    }

    @Override
    public BytecodeParser.Tree visitIntegerTree(BytecodeParser.IntegerTree integerTree, Void unused) {
        return new BytecodeParser.IntegerTree(integerTree.value);
    }

    @Override
    public BytecodeParser.Tree visitBooleanTree(BytecodeParser.BooleanTree booleanTree, Void unused) {
        return new BytecodeParser.BooleanTree(booleanTree.value);
    }

    @Override
    public BytecodeParser.Tree visitConstantTree(BytecodeParser.ConstantTree constantTree, Void unused) {
        return new BytecodeParser.ConstantTree(constantTree.address());
    }

    @Override
    public BytecodeParser.Tree visitLoadLocalTree(BytecodeParser.LoadLocalTree loadLocalTree, Void unused) {
        return new BytecodeParser.LoadLocalTree(loadLocalTree.address());
    }

    @Override
    public BytecodeParser.Tree visitStoreLocalTree(BytecodeParser.StoreLocalTree storeLocalTree, Void unused) {
        return new BytecodeParser.StoreLocalTree(storeLocalTree.address, scan(storeLocalTree.child));
    }

    @Override
    public BytecodeParser.Tree visitLoadGlobalTree(BytecodeParser.LoadGlobalTree loadGlobalTree, Void unused) {
        return new BytecodeParser.LoadGlobalTree(loadGlobalTree.address());
    }

    @Override
    public BytecodeParser.Tree visitStoreGlobalTree(BytecodeParser.StoreGlobalTree storeGlobalTree, Void unused) {
        return new BytecodeParser.StoreGlobalTree(storeGlobalTree.address, scan(storeGlobalTree.child));
    }

    @Override
    public BytecodeParser.Tree visitBinaryOperationTree(BytecodeParser.BinaryOperationTree operationTree, Void unused) {
        return new BytecodeParser.BinaryOperationTree(scan(operationTree.left), scan(operationTree.right), operationTree.operation);
    }

    @Override
    public BytecodeParser.Tree visitUnaryOperationTree(BytecodeParser.UnaryOperationTree operationTree, Void unused) {
        return new BytecodeParser.UnaryOperationTree(scan(operationTree.exp), operationTree.operation);
    }

    @Override
    public BytecodeParser.Tree visitThisTree(BytecodeParser.ThisTree thisTree, Void unused) {
        return new BytecodeParser.ThisTree();
    }

    @Override
    public BytecodeParser.Tree visitEqualsTree(BytecodeParser.EqualsTree equalsTree, Void unused) {
        return new BytecodeParser.EqualsTree(scan(equalsTree.left), scan(equalsTree.right), equalsTree.operation, equalsTree.equals);
    }

    @Override
    public BytecodeParser.Tree visitCallTree(BytecodeParser.CallTree callTree, Void unused) {
        BytecodeParser.CallTree callTree1 = new BytecodeParser.CallTree(scan(callTree.called), new LinkedList<>());
        for (BytecodeParser.Tree arg : callTree.arguments)
            callTree1.arguments.add(scan(arg));
        return callTree1;
    }

    @Override
    public BytecodeParser.Tree visitCallSuperTree(BytecodeParser.CallSuperTree callSuperTree, Void unused) {
        BytecodeParser.CallSuperTree callTree1 = new BytecodeParser.CallSuperTree(new LinkedList<>());
        for (BytecodeParser.Tree arg : callTree1.arguments())
            callTree1.arguments().add(scan(arg));
        return callTree1;
    }

    @Override
    public BytecodeParser.Tree visitArgumentTree(BytecodeParser.ArgumentTree argumentTree, Void unused) {
        return new BytecodeParser.ArgumentTree(argumentTree.address, scan(argumentTree.exp));
    }

    @Override
    public BytecodeParser.Tree visitGetTypeTree(BytecodeParser.GetTypeTree getTypeTree, Void unused) {
        return new BytecodeParser.GetTypeTree(scan(getTypeTree.exp));
    }

    @Override
    public BytecodeParser.Tree visitThrowTree(BytecodeParser.ThrowTree throwTree, Void unused) {
        return new BytecodeParser.ThrowTree(scan(throwTree.exp));
    }

    @Override
    public BytecodeParser.Tree visitArrayTree(BytecodeParser.ArrayTree arrayTree, Void unused) {
        BytecodeParser.ArrayTree arrayTree1 = new BytecodeParser.ArrayTree(new LinkedList<>());
        for (BytecodeParser.Tree arg : arrayTree.arguments())
            arrayTree1.arguments().add(scan(arg));
        return arrayTree1;
    }

    @Override
    public BytecodeParser.Tree visitDictionaryTree(BytecodeParser.DictionaryTree dictionaryTree, Void unused) {
        BytecodeParser.DictionaryTree dictionaryTree1 = new BytecodeParser.DictionaryTree(new LinkedHashMap<>());
        for (Map.Entry<BytecodeParser.Tree, BytecodeParser.Tree> entry : dictionaryTree.arguments.entrySet())
            dictionaryTree1.arguments.put(scan(entry.getKey()), scan(entry.getValue()));
        return dictionaryTree1;
    }

    @Override
    public BytecodeParser.Tree visitRangeTree(BytecodeParser.RangeTree rangeTree, Void unused) {
        return new BytecodeParser.RangeTree(scan(rangeTree.from), scan(rangeTree.to));
    }

    @Override
    public BytecodeParser.Tree visitNewLineTree(BytecodeParser.NewLineTree newLineTree, Void unused) {
        return new BytecodeParser.NewLineTree(newLineTree.line());
    }

    @Override
    public BytecodeParser.Tree visitLoadMemberFastTree(BytecodeParser.LoadMemberFastTree loadTree, Void unused) {
        return new BytecodeParser.LoadMemberFastTree(loadTree.address());
    }

    @Override
    public BytecodeParser.Tree visitStoreMemberFastTree(BytecodeParser.StoreMemberFastTree storeTree, Void unused) {
        return new BytecodeParser.StoreMemberFastTree(storeTree.address, scan(storeTree.child));
    }

    @Override
    public BytecodeParser.Tree visitLoadMemberTree(BytecodeParser.LoadMemberTree loadTree, Void unused) {
        return new BytecodeParser.LoadMemberTree(loadTree.address, scan(loadTree.exp));
    }

    @Override
    public BytecodeParser.Tree visitStoreMemberTree(BytecodeParser.StoreMemberTree storeTree, Void unused) {
        return new BytecodeParser.StoreMemberTree(storeTree.address, scan(storeTree.child));
    }

    @Override
    public BytecodeParser.Tree visitLoadStaticTree(BytecodeParser.LoadStaticTree loadTree, Void unused) {
        return new BytecodeParser.LoadStaticTree(loadTree.address());
    }

    @Override
    public BytecodeParser.Tree visitStoreStaticTree(BytecodeParser.StoreStaticTree storeTree, Void unused) {
        return new BytecodeParser.StoreStaticTree(storeTree.address, scan(storeTree.child));
    }

    @Override
    public BytecodeParser.Tree visitWriteContainerTree(BytecodeParser.WriteContainerTree writeTree, Void unused) {
        return new BytecodeParser.WriteContainerTree(scan(writeTree.container), scan(writeTree.key), scan(writeTree.exp));
    }

    @Override
    public BytecodeParser.Tree visitReadContainerTree(BytecodeParser.ReadContainerTree readTree, Void unused) {
        return new BytecodeParser.ReadContainerTree(scan(readTree.container), scan(readTree.key));
    }

    @Override
    public BytecodeParser.Tree visitLoadAbstractImplTree(BytecodeParser.LoadAbstractImplTree loadTree, Void unused) {
        return new BytecodeParser.LoadAbstractImplTree(loadTree.name());
    }

    @Override
    public BytecodeParser.Tree visitTryCatchTree(BytecodeParser.TryCatchTree tryCatchTree, Void unused) {
        return new BytecodeParser.TryCatchTree(scan(tryCatchTree.tryBody), scan(tryCatchTree.catchBody), tryCatchTree.exAddress);
    }

    @Override
    public BytecodeParser.Tree visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, Void unused) {
        return new BytecodeParser.ForLoopTree(scan(forLoopTree.iterable), scan(forLoopTree.body), forLoopTree.address);
    }

    @Override
    public BytecodeParser.Tree visitIfElseTree(BytecodeParser.IfElseTree ifElseTree, Void unused) {
        return new BytecodeParser.IfElseTree(scan(ifElseTree.condition), ifElseTree.ifTrue, scan(ifElseTree.ifBody), scan(ifElseTree.elseBody));
    }

    @Override
    public BytecodeParser.Tree visitJavaCodeTree(BytecodeParser.JavaCodeTree javaCodeTree, Void unused) {
        return javaCodeTree; // tree is immutable
    }
}

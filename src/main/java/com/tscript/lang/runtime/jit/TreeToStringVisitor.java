package com.tscript.lang.runtime.jit;

import java.util.Iterator;

public class TreeToStringVisitor extends SimpleTreeVisitor<String, Void> {

    int intent = 0;

    private String getIntent(){
        return " ".repeat(3).repeat(intent);
    }

    @Override
    public String visitRootTree(BytecodeParser.RootTree rootTree, Void unused) {
        return scan(rootTree.tree);
    }

    @Override
    public String visitSequenceTree(BytecodeParser.SequenceTree sequenceTree, Void unused) {
        return scanSequence(sequenceTree.children, getIntent(), "\n", "");
    }

    @Override
    public String visitStringTree(BytecodeParser.StringTree stringTree, Void unused) {
        return "\"" + stringTree.value + "\"";
    }

    @Override
    public String visitIntegerTree(BytecodeParser.IntegerTree integerTree, Void unused) {
        return Integer.toString(integerTree.value);
    }

    @Override
    public String visitRealTree(BytecodeParser.RealTree realTree, Void unused) {
        return Double.toString(realTree.value);
    }

    @Override
    public String visitBooleanTree(BytecodeParser.BooleanTree booleanTree, Void unused) {
        return Boolean.toString(booleanTree.value);
    }

    @Override
    public String visitNullTree(BytecodeParser.NullTree nullTree, Void unused) {
        return "null";
    }

    @Override
    public String visitGetTypeTree(BytecodeParser.GetTypeTree getTypeTree, Void unused) {
        return "typeof " + scan(getTypeTree.exp);
    }

    @Override
    public String visitArgumentTree(BytecodeParser.ArgumentTree argumentTree, Void unused) {
        return scan(argumentTree.exp);
    }

    @Override
    public String visitNewLineTree(BytecodeParser.NewLineTree newLineTree, Void unused) {
        return "new_line: " + newLineTree.line();
    }

    @Override
    public String visitReturnTree(BytecodeParser.ReturnTree returnTree, Void unused) {
        return "return " + scan(returnTree.expression);
    }

    @Override
    public String visitThisTree(BytecodeParser.ThisTree thisTree, Void unused) {
        return "this";
    }

    @Override
    public String visitJavaCodeTree(BytecodeParser.JavaCodeTree javaCodeTree, Void unused) {
        return javaCodeTree.code();
    }

    @Override
    public String visitEqualsTree(BytecodeParser.EqualsTree equalsTree, Void unused) {
        String eq = equalsTree.equals ? "==" : "!=";
        return scan(equalsTree.left) + eq + scan(equalsTree.right);
    }

    @Override
    public String visitRangeTree(BytecodeParser.RangeTree rangeTree, Void unused) {
        return scan(rangeTree.from) + ":" + scan(rangeTree.to);
    }

    @Override
    public String visitArrayTree(BytecodeParser.ArrayTree arrayTree, Void unused) {
        return scanSequence(arrayTree.arguments(), "[", ", ", "]");
    }

    @Override
    public String visitCallTree(BytecodeParser.CallTree callTree, Void unused) {
        return scanSequence(callTree.arguments, "(", ", ", ")");
    }

    @Override
    public String visitCallSuperTree(BytecodeParser.CallSuperTree callSuperTree, Void unused) {
        return "super" + scanSequence(callSuperTree.arguments(), "(", ", ", ")");
    }

    @Override
    public String visitThrowTree(BytecodeParser.ThrowTree throwTree, Void unused) {
        return "throw " + scan(throwTree.exp);
    }

    @Override
    public String visitStoreLocalTree(BytecodeParser.StoreLocalTree storeLocalTree, Void unused) {
        return "locals[" + storeLocalTree.address + "] = " + scan(storeLocalTree.child);
    }

    @Override
    public String visitLoadLocalTree(BytecodeParser.LoadLocalTree loadLocalTree, Void unused) {
        return "locals[" + loadLocalTree.address() + "]";
    }

    @Override
    public String visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, Void unused) {
        intent += 1;
        String storeCmd = forLoopTree.address < 0 ? "" : getIntent() + "locals[" + forLoopTree.address + "] = ITR_NEXT\n";
        String s = "for " + scan(forLoopTree.iterable) + ":\n" +
                storeCmd +
                scan(forLoopTree.body);
        intent -= 1;
        return s;
    }

    @Override
    public String visitBinaryOperationTree(BytecodeParser.BinaryOperationTree operationTree, Void unused) {
        return "(" + scan(operationTree.left) + " " + operationTree.operation + " " + scan(operationTree.right) + ")";
    }

    @Override
    public String visitUnaryOperationTree(BytecodeParser.UnaryOperationTree operationTree, Void unused) {
        return "(" + operationTree.operation + " " + scan(operationTree.exp) + ")";
    }

    @Override
    public String visitConstantTree(BytecodeParser.ConstantTree constantTree, Void unused) {
        return "constant[" + constantTree.address() + "]";
    }

    private String scanSequence(Iterable<BytecodeParser.Tree> sequence, String prefix, String suffix, String postFix){
        StringBuilder sb = new StringBuilder(prefix);
        Iterator<BytecodeParser.Tree> itr = sequence.iterator();
        if (itr.hasNext())
            sb.append(scan(itr.next()));
        while (itr.hasNext()){
            sb.append(suffix).append(scan(itr.next()));
        }
        return sb.append(postFix).toString();
    }
}

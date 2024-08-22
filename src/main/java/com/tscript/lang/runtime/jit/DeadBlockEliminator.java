package com.tscript.lang.runtime.jit;

public class DeadBlockEliminator extends ParentDelegationPhase<Void> {


    @Override
    public Void visitIfElseTree(BytecodeParser.IfElseTree ifElseTree, BytecodeParser.Tree parent) {
        scan(ifElseTree.condition, ifElseTree);
        scan(ifElseTree.ifBody, ifElseTree);
        scan(ifElseTree.elseBody, ifElseTree);

        if (isTrue(ifElseTree.condition)){
            optimizationPerformed();
            parent.replace(ifElseTree, ifElseTree.ifBody);
            return null;
        }
        else if (isFalse(ifElseTree.condition)){
            optimizationPerformed();
            if (ifElseTree.elseBody != null)
                parent.replace(ifElseTree, ifElseTree.elseBody);
            else
                parent.remove(ifElseTree);
            return null;
        }

        if (ifElseTree.elseBody instanceof BytecodeParser.SequenceTree s){
            if (isEmpty(s)) {
                ifElseTree.elseBody = null;
                optimizationPerformed();
            }
        }

        if (ifElseTree.ifBody instanceof BytecodeParser.SequenceTree s){
            if (isEmpty(s)){
                if (ifElseTree.elseBody == null) {
                    parent.remove(ifElseTree);
                }
                else {
                    ifElseTree.ifBody = ifElseTree.elseBody;
                    ifElseTree.elseBody = null;
                    ifElseTree.ifTrue = !ifElseTree.ifTrue;
                }
                optimizationPerformed();
            }
        }

        return null;
    }

    @Override
    public Void visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, BytecodeParser.Tree parent) {
        scan(forLoopTree.iterable, forLoopTree);
        scan(forLoopTree.body, forLoopTree);

        if (forLoopTree.body instanceof BytecodeParser.SequenceTree s){
            if (isEmpty(s)){
                parent.remove(forLoopTree);
                optimizationPerformed();
                return null;
            }
        }

        return null;
    }


    @Override
    public Void visitTryCatchTree(BytecodeParser.TryCatchTree tryCatchTree, BytecodeParser.Tree parent) {
        scan(tryCatchTree.tryBody, tryCatchTree);
        scan(tryCatchTree.catchBody, tryCatchTree);

        if (tryCatchTree.tryBody instanceof BytecodeParser.SequenceTree s){
            if (isEmpty(s)){
                parent.remove(tryCatchTree);
                optimizationPerformed();
            }
        }

        return null;
    }

    private boolean isTrue(BytecodeParser.Tree tree){
        return (tree instanceof BytecodeParser.BooleanTree b && b.value)
                || (tree instanceof BytecodeParser.IntegerTree i && i.value != 0)
                || (tree instanceof BytecodeParser.ArrayTree a && !a.arguments().isEmpty())
                || (tree instanceof BytecodeParser.DictionaryTree d && !d.arguments.isEmpty());
    }

    private boolean isFalse(BytecodeParser.Tree tree){
        return (tree instanceof BytecodeParser.BooleanTree b && !b.value)
                || (tree instanceof BytecodeParser.IntegerTree i && i.value == 0)
                || (tree instanceof BytecodeParser.ArrayTree a && a.arguments().isEmpty())
                || (tree instanceof BytecodeParser.DictionaryTree d && d.arguments.isEmpty());
    }

    private boolean isEmpty(BytecodeParser.SequenceTree s){
        if (s.children.isEmpty()) return true;
        for (BytecodeParser.Tree child : s.children)
            if (child instanceof BytecodeParser.SequenceTree ss)
                if (isEmpty(ss))
                    return true;
        return false;
    }
}

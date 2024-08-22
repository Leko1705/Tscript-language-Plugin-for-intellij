package com.tscript.lang.runtime.jit;

public class LoopUnoller extends ParentDelegationPhase<Void> {

    @Override
    public Void visitForLoopTree(BytecodeParser.ForLoopTree forLoopTree, BytecodeParser.Tree parent) {

        if (forLoopTree.iterable instanceof BytecodeParser.RangeTree r){

            if (r.from instanceof BytecodeParser.IntegerTree f
                    && r.to instanceof BytecodeParser.IntegerTree t
                    && t.value - f.value <= 100){
                BytecodeParser.SequenceTree unrolled = new BytecodeParser.SequenceTree();
                for (int i = f.value; i < t.value; i++) {
                    unrolled.children.add(new BytecodeParser.StoreLocalTree(forLoopTree.address, new BytecodeParser.IntegerTree(i)));
                    BytecodeParser.Tree copy = TreeCopier.copy(forLoopTree.body);
                    unrolled.children.add(copy);
                }

                parent.replace(forLoopTree, unrolled);
                optimizationPerformed();
            }
        }

        return null;
    }

}

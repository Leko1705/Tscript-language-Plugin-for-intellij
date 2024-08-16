package com.test.exec.tscript.runtime.jit;

import com.test.exec.tscript.runtime.core.Data;
import com.test.exec.tscript.runtime.heap.Heap;

import java.util.List;

public class Optimizer {

    private static final List<OptimizationPhase<?, ?>> phases =
            List.of(
                    new LoopUnoller(),
                    new ConstantFolder(), new DeadVariableEliminator(),
                    new DeadBlockEliminator(), new UnsafeReducer(), new TypeReducer());

    public static BytecodeParser.Tree optimize(BytecodeParser.Tree tree, Data[] args, JIT jit){
        boolean optimizationPerformed;
        do {
            optimizationPerformed = false;
            for (OptimizationPhase<?, ?> phase : phases){
                phase.reset();
                tree = phase.performOptimization(tree, args, jit);
                optimizationPerformed = optimizationPerformed || phase.isOptimizationPerformed();
            }
        }while (optimizationPerformed);
        return tree;
    }

}

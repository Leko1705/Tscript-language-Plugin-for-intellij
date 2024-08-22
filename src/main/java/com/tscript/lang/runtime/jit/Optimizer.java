package com.tscript.lang.runtime.jit;

import com.tscript.lang.runtime.core.Data;

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

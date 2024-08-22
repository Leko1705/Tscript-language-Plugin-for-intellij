package com.tscript.lang.runtime.jit;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.VirtualFunction;

public record OptimizeVirtualTask(VirtualFunction called, Data[] args) implements JITTask {

    @Override
    public void handle(JIT jit) {
        if (true) return;
        if (jit.hasOptimization(called, args)) return;
        BytecodeParser.Tree tree = BytecodeParser.parse(called);
        tree = Optimizer.optimize(tree, args, jit);
        String fileName = called.getName().replaceAll(" ", "_");
        String javaCode = JavaCodeGenerator.generate(fileName, tree, called);
        //Callable optimized = java.lang.Compiler.compile(fileName, javaCode);
        //jit.setOptimized(called, optimized, args);
    }

}

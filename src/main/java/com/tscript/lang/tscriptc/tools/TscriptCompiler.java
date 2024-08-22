package com.tscript.lang.tscriptc.tools;


import com.tscript.lang.tscriptc.analysis.EscapeChecker;
import com.tscript.lang.tscriptc.generation.Compiled;
import com.tscript.lang.tscriptc.generation.TscriptBytecodeGenerator;
import com.tscript.lang.tscriptc.log.Logger;
import com.tscript.lang.tscriptc.log.StdLogger;

import com.tscript.lang.tscriptc.parse.*;
import com.tscript.lang.tscriptc.tree.RootTree;
import com.tscript.lang.tscriptc.analysis.Checker;
import com.tscript.lang.tscriptc.analysis.DefinitionChecker;
import com.tscript.lang.tscriptc.analysis.TypeChecker;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class TscriptCompiler implements Compiler {

    private final List<Checker<?, ?>> checkers =
            List.of(new DefinitionChecker(), new EscapeChecker(), new TypeChecker());

    private Compiled compiled;

    private Set<Integer> breakPoints = Set.of();

    public TscriptCompiler(){}

    @Override
    public String getName() {
        return "tscriptc";
    }

    @Override
    public int run(InputStream in, OutputStream out, Logger logger, String... args) {

        compiled = compile0(in, logger, args);
        if (compiled == null) return -1;

        try {
            compiled.write(out);
        }catch (Exception ignored){}

        return 0;
    }

    @Override
    public int dis(InputStream in, OutputStream out, Logger logger, String... args) {
        if (compiled == null) {
            compiled = compile0(in, logger, args);
            if (compiled == null)
                return -1;
        }

        try {
            compiled.writeReadable(out);
        }catch (Exception ignored){}

        return 1;
    }

    public Compiled compile0(InputStream in, Logger logger, String... args) {
        Objects.requireNonNull(in, "InputStream must not be null");

        if (logger == null)
            logger = StdLogger.getLogger();

        loadBreakPoints(args);

        RootTree ast = parse(in, logger, breakPoints);
        if (!analyze(ast, logger)) return null;

        TscriptBytecodeGenerator generator = new TscriptBytecodeGenerator();
        ast.accept(generator);
        return generator.getCompiled();
    }

    private void loadBreakPoints(String[] args){
        breakPoints = new HashSet<>();
        for (String arg : args)
            breakPoints.add(Integer.parseInt(arg));

    }

    private RootTree parse(InputStream in, Logger logger, Set<Integer> breakPoints){
        UnicodeReader reader = new UnicodeReader(in);
        Lexer lexer = new Scanner(reader, logger);
        Parser parser = new TscriptParser(lexer, logger, breakPoints);
        return parser.parseProgram();
    }

    private boolean analyze(RootTree rootTree, Logger logger){
        for (Checker<?, ?> checker : checkers)
            if (!checker.check(rootTree, logger))
                return false;
        return true;
    }

}

package com.tscript.lang.tscriptc.tools;

import com.tscript.lang.tscriptc.analysis.Checker;
import com.tscript.lang.tscriptc.analysis.DefinitionChecker;
import com.tscript.lang.tscriptc.analysis.EscapeChecker;
import com.tscript.lang.tscriptc.analysis.TypeChecker;
import com.tscript.lang.tscriptc.generation.TscriptGenerator;
import com.tscript.lang.tscriptc.log.Logger;
import com.tscript.lang.tscriptc.log.StdLogger;

import com.tscript.lang.tscriptc.parse.*;
import com.tscript.lang.tscriptc.tree.RootTree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ToWebTscriptConverter implements Compiler {

    private final List<Checker<?, ?>> checkers =
            List.of(new DefinitionChecker(), new EscapeChecker(), new TypeChecker());

    @Override
    public String getName() {
        return "tscript-to-web-tscript-converter";
    }

    @Override
    public int run(InputStream in, OutputStream out, Logger logger, String... args) {
        Objects.requireNonNull(in, "InputStream must not be null");
        Objects.requireNonNull(out, "OutputStream must not be null");

        if (logger == null)
            logger = StdLogger.getLogger();

        RootTree ast = parse(in, logger);
        if (!analyze(ast, logger)) return -1;

        TscriptGenerator generator = new TscriptGenerator(true);
        String generated = generator.generate(ast);
        try {
            out.write(generated.getBytes(StandardCharsets.UTF_8));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private RootTree parse(InputStream in, Logger logger){
        UnicodeReader reader = new UnicodeReader(in);
        Lexer lexer = new Scanner(reader, logger);
        Parser parser = new TscriptParser(lexer, logger, new HashSet<>());
        return parser.parseProgram();
    }

    private boolean analyze(RootTree rootTree, Logger logger){
        for (Checker<?, ?> checker : checkers)
            if (!checker.check(rootTree, logger))
                return false;
        return true;
    }
}

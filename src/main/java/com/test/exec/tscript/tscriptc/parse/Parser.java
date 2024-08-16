package com.test.exec.tscript.tscriptc.parse;

import com.test.exec.tscript.tscriptc.tree.ExpressionTree;
import com.test.exec.tscript.tscriptc.tree.RootTree;
import com.test.exec.tscript.tscriptc.tree.StatementTree;

public interface Parser {

    RootTree parseProgram();

    StatementTree parseStatement();

    ExpressionTree parseExpression();

}

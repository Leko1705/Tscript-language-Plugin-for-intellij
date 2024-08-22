package com.tscript.lang.tscriptc.parse;

import com.tscript.lang.tscriptc.tree.ExpressionTree;
import com.tscript.lang.tscriptc.tree.RootTree;
import com.tscript.lang.tscriptc.tree.StatementTree;

public interface Parser {

    RootTree parseProgram();

    StatementTree parseStatement();

    ExpressionTree parseExpression();

}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestIfElse extends PsiElement {

  @NotNull
  List<TestBlock> getBlockList();

  @NotNull
  List<TestBreakStmt> getBreakStmtList();

  @NotNull
  List<TestConstDec> getConstDecList();

  @NotNull
  List<TestContinueStmt> getContinueStmtList();

  @NotNull
  List<TestDoWhile> getDoWhileList();

  @NotNull
  List<TestExpr> getExprList();

  @NotNull
  List<TestForLoop> getForLoopList();

  @NotNull
  List<TestFunctionDef> getFunctionDefList();

  @NotNull
  List<TestIfElse> getIfElseList();

  @NotNull
  List<TestReturnStmt> getReturnStmtList();

  @NotNull
  List<TestThrowStmt> getThrowStmtList();

  @NotNull
  List<TestTryCatch> getTryCatchList();

  @NotNull
  List<TestVarDec> getVarDecList();

  @NotNull
  List<TestWhileDo> getWhileDoList();

}

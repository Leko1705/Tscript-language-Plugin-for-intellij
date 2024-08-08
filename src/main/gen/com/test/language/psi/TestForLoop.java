// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestForLoop extends PsiElement {

  @Nullable
  TestBlock getBlock();

  @Nullable
  TestBreakStmt getBreakStmt();

  @Nullable
  TestConstDec getConstDec();

  @Nullable
  TestContinueStmt getContinueStmt();

  @Nullable
  TestDoWhile getDoWhile();

  @NotNull
  List<TestExpr> getExprList();

  @Nullable
  TestForLoop getForLoop();

  @Nullable
  TestFunctionDef getFunctionDef();

  @Nullable
  TestIfElse getIfElse();

  @Nullable
  TestReturnStmt getReturnStmt();

  @Nullable
  TestThrowStmt getThrowStmt();

  @Nullable
  TestTryCatch getTryCatch();

  @Nullable
  TestVarDec getVarDec();

  @Nullable
  TestWhileDo getWhileDo();

}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestWhileDo extends PsiElement {

  @Nullable
  TestBlock getBlock();

  @Nullable
  TestCostDec getCostDec();

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
  TestThrowStmt getThrowStmt();

  @Nullable
  TestTryCatch getTryCatch();

  @Nullable
  TestVarDec getVarDec();

  @Nullable
  TestWhileDo getWhileDo();

}

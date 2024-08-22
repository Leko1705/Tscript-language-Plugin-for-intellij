// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestStmt extends PsiElement {

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

  @Nullable
  TestExpr getExpr();

  @Nullable
  TestForLoop getForLoop();

  @Nullable
  TestFromImport getFromImport();

  @Nullable
  TestFromUse getFromUse();

  @Nullable
  TestFunctionDef getFunctionDef();

  @Nullable
  TestIfElse getIfElse();

  @Nullable
  TestImportStmt getImportStmt();

  @Nullable
  TestReturnStmt getReturnStmt();

  @Nullable
  TestThrowStmt getThrowStmt();

  @Nullable
  TestTryCatch getTryCatch();

  @Nullable
  TestUseStmt getUseStmt();

  @Nullable
  TestVarDec getVarDec();

  @Nullable
  TestWhileDo getWhileDo();

}

// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestMulExpr extends TestExpr {

  @NotNull
  List<TestExpr> getExprList();

  @NotNull
  List<TestMulOp> getMulOpList();

}

// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestLambdaExpr extends TestExpr {

  @Nullable
  TestBlock getBlock();

  @NotNull
  List<TestClosure> getClosureList();

  @NotNull
  List<TestParam> getParamList();

}

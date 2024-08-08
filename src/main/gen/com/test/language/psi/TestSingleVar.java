// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.test.language.psi.TestNamedElements.VariableDef;
import com.intellij.util.IncorrectOperationException;

public interface TestSingleVar extends VariableDef {

  @NotNull
  List<TestExpr> getExprList();

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  @Nullable PsiElement getNameIdentifier();

}

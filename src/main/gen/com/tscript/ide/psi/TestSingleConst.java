// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tscript.ide.psi.MixinElements.VariableDef;
import com.intellij.util.IncorrectOperationException;

public interface TestSingleConst extends VariableDef {

  @Nullable
  TestExpr getExpr();

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  @Nullable PsiElement getNameIdentifier();

}

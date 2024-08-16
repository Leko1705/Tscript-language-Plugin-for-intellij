// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.test.language.psi.MixinElements.ForLoop;
import com.intellij.util.IncorrectOperationException;

public interface TestForLoop extends ForLoop {

  @Nullable
  TestExpr getExpr();

  @Nullable
  TestStmt getStmt();

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  @Nullable PsiElement getNameIdentifier();

}

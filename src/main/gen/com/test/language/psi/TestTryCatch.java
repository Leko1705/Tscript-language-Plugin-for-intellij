// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.test.language.psi.MixinElements.TryCatch;
import com.intellij.util.IncorrectOperationException;

public interface TestTryCatch extends TryCatch {

  @NotNull
  List<TestStmt> getStmtList();

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  @Nullable PsiElement getNameIdentifier();

}

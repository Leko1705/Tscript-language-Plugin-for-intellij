// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.test.language.psi.MixinElements.SuperMemAccess;
import com.intellij.util.IncorrectOperationException;

public interface TestSuperAccessExpr extends TestExpr, SuperMemAccess {

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  @Nullable PsiElement getNameIdentifier();

}

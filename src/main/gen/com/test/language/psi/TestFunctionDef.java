// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestFunctionDef extends com.test.language.psi.TestNamedElements.TestFunctionDef {

  @Nullable
  TestBlock getBlock();

  @NotNull
  TestParams getParams();

  String getName();

  PsiElement setName(@NotNull String newName);

  @Nullable PsiElement getNameIdentifier();

}

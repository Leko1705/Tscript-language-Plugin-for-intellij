// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestFunctionDef extends com.tscript.ide.psi.MixinElements.TestFunctionDef {

  @Nullable
  TestBlock getBlock();

  @NotNull
  List<TestParam> getParamList();

  String getName();

  PsiElement setName(@NotNull String newName);

  @Nullable PsiElement getNameIdentifier();

  PsiElement getStaticElement();

  PsiElement getOverriddenElement();

  PsiElement getNativeElement();

  PsiElement getAbstractElement();

}

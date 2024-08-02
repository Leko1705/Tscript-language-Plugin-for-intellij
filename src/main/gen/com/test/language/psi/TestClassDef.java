// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestClassDef extends com.test.language.psi.TestNamedElements.TestClassDef {

  @NotNull
  List<TestClassDef> getClassDefList();

  @NotNull
  List<TestConstructorDef> getConstructorDefList();

  @NotNull
  List<TestCostDec> getCostDecList();

  @NotNull
  List<TestFunctionDef> getFunctionDefList();

  @NotNull
  List<TestNamespaceDef> getNamespaceDefList();

  @NotNull
  List<TestVarDec> getVarDecList();

  @NotNull
  List<TestVisibility> getVisibilityList();

  String getName();

  @Nullable PsiElement getNameIdentifier();

  String getSuperName();

  PsiElement getSuperClassIdentifier();

}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.test.language.psi.TestNamedElements.TestNSpaceDef;

public interface TestNamespaceDef extends TestNSpaceDef {

  @NotNull
  List<TestClassDef> getClassDefList();

  @NotNull
  List<TestFunctionDef> getFunctionDefList();

  @NotNull
  List<TestNamespaceDef> getNamespaceDefList();

  String getName();

  @Nullable PsiElement getNameIdentifier();

  PsiElement getStaticElement();

}

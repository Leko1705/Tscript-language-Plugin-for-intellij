// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestNamespaceDef extends PsiElement {

  @NotNull
  List<TestClassDef> getClassDefList();

  @NotNull
  List<TestFunctionDef> getFunctionDefList();

  @NotNull
  List<TestNamespaceDef> getNamespaceDefList();

}

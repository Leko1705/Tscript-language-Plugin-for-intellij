// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestClassBodyDef extends PsiElement {

  @NotNull
  List<TestConstDec> getConstDecList();

  @NotNull
  List<TestConstructorDef> getConstructorDefList();

  @NotNull
  List<TestDefinition> getDefinitionList();

  @NotNull
  List<TestVarDec> getVarDecList();

  @NotNull
  List<TestVisibility> getVisibilityList();

}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestArray extends PsiElement {

  @NotNull
  List<TestArray> getArrayList();

  @NotNull
  List<TestCall> getCallList();

  @NotNull
  List<TestContainerAccess> getContainerAccessList();

  @NotNull
  List<TestDictionary> getDictionaryList();

  @NotNull
  List<TestLambda> getLambdaList();

  @NotNull
  List<TestMemberAccess> getMemberAccessList();

  @NotNull
  List<TestSuperAccess> getSuperAccessList();

  @NotNull
  List<TestTypeofPrefixExpr> getTypeofPrefixExprList();

}

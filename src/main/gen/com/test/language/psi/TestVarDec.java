// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestVarDec extends PsiElement {

  @Nullable
  TestArray getArray();

  @NotNull
  List<TestCall> getCallList();

  @NotNull
  List<TestContainerAccess> getContainerAccessList();

  @Nullable
  TestDictionary getDictionary();

  @Nullable
  TestLambda getLambda();

  @NotNull
  List<TestMemberAccess> getMemberAccessList();

  @Nullable
  TestSuperAccess getSuperAccess();

  @Nullable
  TestTypeofPrefixExpr getTypeofPrefixExpr();

}

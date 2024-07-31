// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestDoWhile extends PsiElement {

  @NotNull
  List<TestArray> getArrayList();

  @Nullable
  TestBlock getBlock();

  @NotNull
  List<TestCall> getCallList();

  @NotNull
  List<TestContainerAccess> getContainerAccessList();

  @Nullable
  TestCostDec getCostDec();

  @NotNull
  List<TestDictionary> getDictionaryList();

  @Nullable
  TestDoWhile getDoWhile();

  @Nullable
  TestForLoop getForLoop();

  @Nullable
  TestFunctionDef getFunctionDef();

  @Nullable
  TestIfElse getIfElse();

  @NotNull
  List<TestLambda> getLambdaList();

  @NotNull
  List<TestMemberAccess> getMemberAccessList();

  @NotNull
  List<TestSuperAccess> getSuperAccessList();

  @Nullable
  TestThrowStmt getThrowStmt();

  @Nullable
  TestTryCatch getTryCatch();

  @NotNull
  List<TestTypeofPrefixExpr> getTypeofPrefixExprList();

  @Nullable
  TestVarDec getVarDec();

  @Nullable
  TestWhileDo getWhileDo();

}

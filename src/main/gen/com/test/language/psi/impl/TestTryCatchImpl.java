// This is a generated file. Not intended for manual editing.
package com.test.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.test.language.psi.TestTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.test.language.psi.*;

public class TestTryCatchImpl extends ASTWrapperPsiElement implements TestTryCatch {

  public TestTryCatchImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitTryCatch(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestArray> getArrayList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestArray.class);
  }

  @Override
  @NotNull
  public List<TestBlock> getBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestBlock.class);
  }

  @Override
  @NotNull
  public List<TestCall> getCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestCall.class);
  }

  @Override
  @NotNull
  public List<TestContainerAccess> getContainerAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestContainerAccess.class);
  }

  @Override
  @NotNull
  public List<TestCostDec> getCostDecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestCostDec.class);
  }

  @Override
  @NotNull
  public List<TestDictionary> getDictionaryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestDictionary.class);
  }

  @Override
  @NotNull
  public List<TestDoWhile> getDoWhileList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestDoWhile.class);
  }

  @Override
  @NotNull
  public List<TestForLoop> getForLoopList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestForLoop.class);
  }

  @Override
  @NotNull
  public List<TestFunctionDef> getFunctionDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestFunctionDef.class);
  }

  @Override
  @NotNull
  public List<TestIfElse> getIfElseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestIfElse.class);
  }

  @Override
  @NotNull
  public List<TestLambda> getLambdaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestLambda.class);
  }

  @Override
  @NotNull
  public List<TestMemberAccess> getMemberAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestMemberAccess.class);
  }

  @Override
  @NotNull
  public List<TestSuperAccess> getSuperAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestSuperAccess.class);
  }

  @Override
  @NotNull
  public List<TestThrowStmt> getThrowStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestThrowStmt.class);
  }

  @Override
  @NotNull
  public List<TestTryCatch> getTryCatchList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestTryCatch.class);
  }

  @Override
  @NotNull
  public List<TestTypeofPrefixExpr> getTypeofPrefixExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestTypeofPrefixExpr.class);
  }

  @Override
  @NotNull
  public List<TestVarDec> getVarDecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestVarDec.class);
  }

  @Override
  @NotNull
  public List<TestWhileDo> getWhileDoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestWhileDo.class);
  }

}

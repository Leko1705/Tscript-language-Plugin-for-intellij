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

public class TestIfElseImpl extends ASTWrapperPsiElement implements TestIfElse {

  public TestIfElseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitIfElse(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestBlock> getBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestBlock.class);
  }

  @Override
  @NotNull
  public List<TestCostDec> getCostDecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestCostDec.class);
  }

  @Override
  @NotNull
  public List<TestDoWhile> getDoWhileList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestDoWhile.class);
  }

  @Override
  @NotNull
  public List<TestExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestExpr.class);
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
  public List<TestVarDec> getVarDecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestVarDec.class);
  }

  @Override
  @NotNull
  public List<TestWhileDo> getWhileDoList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestWhileDo.class);
  }

}

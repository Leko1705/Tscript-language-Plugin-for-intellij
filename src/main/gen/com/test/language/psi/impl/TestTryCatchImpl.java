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
  public List<TestBlock> getBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestBlock.class);
  }

  @Override
  @NotNull
  public List<TestBreakStmt> getBreakStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestBreakStmt.class);
  }

  @Override
  @NotNull
  public List<TestConstDec> getConstDecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestConstDec.class);
  }

  @Override
  @NotNull
  public List<TestContinueStmt> getContinueStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestContinueStmt.class);
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
  public List<TestReturnStmt> getReturnStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestReturnStmt.class);
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

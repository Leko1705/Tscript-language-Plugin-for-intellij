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

public class TestWhileDoImpl extends ASTWrapperPsiElement implements TestWhileDo {

  public TestWhileDoImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitWhileDo(this);
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
  @Nullable
  public TestBlock getBlock() {
    return findChildByClass(TestBlock.class);
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
  @Nullable
  public TestCostDec getCostDec() {
    return findChildByClass(TestCostDec.class);
  }

  @Override
  @NotNull
  public List<TestDictionary> getDictionaryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestDictionary.class);
  }

  @Override
  @Nullable
  public TestDoWhile getDoWhile() {
    return findChildByClass(TestDoWhile.class);
  }

  @Override
  @Nullable
  public TestForLoop getForLoop() {
    return findChildByClass(TestForLoop.class);
  }

  @Override
  @Nullable
  public TestFunctionDef getFunctionDef() {
    return findChildByClass(TestFunctionDef.class);
  }

  @Override
  @Nullable
  public TestIfElse getIfElse() {
    return findChildByClass(TestIfElse.class);
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
  @Nullable
  public TestThrowStmt getThrowStmt() {
    return findChildByClass(TestThrowStmt.class);
  }

  @Override
  @Nullable
  public TestTryCatch getTryCatch() {
    return findChildByClass(TestTryCatch.class);
  }

  @Override
  @NotNull
  public List<TestTypeofPrefixExpr> getTypeofPrefixExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestTypeofPrefixExpr.class);
  }

  @Override
  @Nullable
  public TestVarDec getVarDec() {
    return findChildByClass(TestVarDec.class);
  }

  @Override
  @Nullable
  public TestWhileDo getWhileDo() {
    return findChildByClass(TestWhileDo.class);
  }

}

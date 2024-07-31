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

public class TestArrayImpl extends ASTWrapperPsiElement implements TestArray {

  public TestArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitArray(this);
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
  public List<TestDictionary> getDictionaryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestDictionary.class);
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
  public List<TestTypeofPrefixExpr> getTypeofPrefixExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestTypeofPrefixExpr.class);
  }

}

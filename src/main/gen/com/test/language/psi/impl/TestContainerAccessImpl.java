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

public class TestContainerAccessImpl extends ASTWrapperPsiElement implements TestContainerAccess {

  public TestContainerAccessImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitContainerAccess(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TestArray getArray() {
    return findChildByClass(TestArray.class);
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
  public TestDictionary getDictionary() {
    return findChildByClass(TestDictionary.class);
  }

  @Override
  @Nullable
  public TestLambda getLambda() {
    return findChildByClass(TestLambda.class);
  }

  @Override
  @NotNull
  public List<TestMemberAccess> getMemberAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestMemberAccess.class);
  }

  @Override
  @Nullable
  public TestSuperAccess getSuperAccess() {
    return findChildByClass(TestSuperAccess.class);
  }

  @Override
  @Nullable
  public TestTypeofPrefixExpr getTypeofPrefixExpr() {
    return findChildByClass(TestTypeofPrefixExpr.class);
  }

}

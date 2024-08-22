// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tscript.ide.psi.TestTypes.*;
import com.tscript.ide.psi.*;

public class TestUnaryExprImpl extends TestExprImpl implements TestUnaryExpr {

  public TestUnaryExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitUnaryExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
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
  public TestExpr getExpr() {
    return findChildByClass(TestExpr.class);
  }

  @Override
  @Nullable
  public TestIdentifier getIdentifier() {
    return findChildByClass(TestIdentifier.class);
  }

  @Override
  @NotNull
  public List<TestMemAccess> getMemAccessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestMemAccess.class);
  }

  @Override
  @Nullable
  public TestSuperAccess getSuperAccess() {
    return findChildByClass(TestSuperAccess.class);
  }

}

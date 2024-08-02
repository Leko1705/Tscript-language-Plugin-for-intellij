// This is a generated file. Not intended for manual editing.
package com.test.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.test.language.psi.TestTypes.*;
import com.test.language.psi.*;

public class TestCompExprImpl extends TestExprImpl implements TestCompExpr {

  public TestCompExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitCompExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestCompOp> getCompOpList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestCompOp.class);
  }

  @Override
  @NotNull
  public List<TestExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestExpr.class);
  }

}

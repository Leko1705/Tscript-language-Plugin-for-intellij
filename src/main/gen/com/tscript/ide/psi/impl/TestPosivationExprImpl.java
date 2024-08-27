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

public class TestPosivationExprImpl extends TestExprImpl implements TestPosivationExpr {

  public TestPosivationExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitPosivationExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
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
  @Nullable
  public TestSuperAccess getSuperAccess() {
    return findChildByClass(TestSuperAccess.class);
  }

}

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

public class TestLambdaExprImpl extends TestExprImpl implements TestLambdaExpr {

  public TestLambdaExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitLambdaExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TestBlock getBlock() {
    return findChildByClass(TestBlock.class);
  }

  @Override
  @NotNull
  public List<TestClosure> getClosureList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestClosure.class);
  }

  @Override
  @NotNull
  public List<TestParam> getParamList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestParam.class);
  }

}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.test.language.psi.TestTypes.*;
import com.test.language.psi.MixinElements.TryCatchMixin;
import com.test.language.psi.*;
import com.intellij.util.IncorrectOperationException;

public class TestTryCatchImpl extends TryCatchMixin implements TestTryCatch {

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
  public List<TestStmt> getStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestStmt.class);
  }

}

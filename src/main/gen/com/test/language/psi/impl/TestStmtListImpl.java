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

public class TestStmtListImpl extends ASTWrapperPsiElement implements TestStmtList {

  public TestStmtListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitStmtList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestVarDec> getVarDecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestVarDec.class);
  }

}

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

public class TestConstructorDefImpl extends ASTWrapperPsiElement implements TestConstructorDef {

  public TestConstructorDefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitConstructorDef(this);
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
  @Nullable
  public TestCall getCall() {
    return findChildByClass(TestCall.class);
  }

  @Override
  @NotNull
  public List<TestParam> getParamList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestParam.class);
  }

}

// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tscript.ide.psi.TestTypes.*;
import com.tscript.ide.psi.MixinElements.VarDefListMixin;
import com.tscript.ide.psi.*;

public class TestConstDecImpl extends VarDefListMixin implements TestConstDec {

  public TestConstDecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitConstDec(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestSingleConst> getSingleConstList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestSingleConst.class);
  }

}

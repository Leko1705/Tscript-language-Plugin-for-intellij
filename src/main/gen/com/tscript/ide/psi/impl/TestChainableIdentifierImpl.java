// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tscript.ide.psi.TestTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.tscript.ide.psi.*;

public class TestChainableIdentifierImpl extends ASTWrapperPsiElement implements TestChainableIdentifier {

  public TestChainableIdentifierImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitChainableIdentifier(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestIdentifier> getIdentifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestIdentifier.class);
  }

}

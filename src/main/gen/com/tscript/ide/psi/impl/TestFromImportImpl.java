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

public class TestFromImportImpl extends ASTWrapperPsiElement implements TestFromImport {

  public TestFromImportImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitFromImport(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestChainableIdentifier> getChainableIdentifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestChainableIdentifier.class);
  }

}

// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tscript.ide.psi.TestTypes.*;
import com.tscript.ide.psi.MixinElements.TestNSpaceDefMixin;
import com.tscript.ide.psi.*;

public class TestNamespaceDefImpl extends TestNSpaceDefMixin implements TestNamespaceDef {

  public TestNamespaceDefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitNamespaceDef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TestVisitor) accept((TestVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TestDefinition> getDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestDefinition.class);
  }

}

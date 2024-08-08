// This is a generated file. Not intended for manual editing.
package com.test.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.test.language.psi.TestTypes.*;
import com.test.language.psi.TestNamedElements.TestNSpaceDefMixin;
import com.test.language.psi.*;

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
  public List<TestClassDef> getClassDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestClassDef.class);
  }

  @Override
  @NotNull
  public List<TestFunctionDef> getFunctionDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestFunctionDef.class);
  }

  @Override
  @NotNull
  public List<TestNamespaceDef> getNamespaceDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TestNamespaceDef.class);
  }

}

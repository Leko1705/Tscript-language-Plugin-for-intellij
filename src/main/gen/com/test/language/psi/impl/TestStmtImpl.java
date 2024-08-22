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

public class TestStmtImpl extends ASTWrapperPsiElement implements TestStmt {

  public TestStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TestVisitor visitor) {
    visitor.visitStmt(this);
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
  public TestBreakStmt getBreakStmt() {
    return findChildByClass(TestBreakStmt.class);
  }

  @Override
  @Nullable
  public TestConstDec getConstDec() {
    return findChildByClass(TestConstDec.class);
  }

  @Override
  @Nullable
  public TestContinueStmt getContinueStmt() {
    return findChildByClass(TestContinueStmt.class);
  }

  @Override
  @Nullable
  public TestDoWhile getDoWhile() {
    return findChildByClass(TestDoWhile.class);
  }

  @Override
  @Nullable
  public TestExpr getExpr() {
    return findChildByClass(TestExpr.class);
  }

  @Override
  @Nullable
  public TestForLoop getForLoop() {
    return findChildByClass(TestForLoop.class);
  }

  @Override
  @Nullable
  public TestFromImport getFromImport() {
    return findChildByClass(TestFromImport.class);
  }

  @Override
  @Nullable
  public TestFromUse getFromUse() {
    return findChildByClass(TestFromUse.class);
  }

  @Override
  @Nullable
  public TestFunctionDef getFunctionDef() {
    return findChildByClass(TestFunctionDef.class);
  }

  @Override
  @Nullable
  public TestIfElse getIfElse() {
    return findChildByClass(TestIfElse.class);
  }

  @Override
  @Nullable
  public TestImportStmt getImportStmt() {
    return findChildByClass(TestImportStmt.class);
  }

  @Override
  @Nullable
  public TestReturnStmt getReturnStmt() {
    return findChildByClass(TestReturnStmt.class);
  }

  @Override
  @Nullable
  public TestThrowStmt getThrowStmt() {
    return findChildByClass(TestThrowStmt.class);
  }

  @Override
  @Nullable
  public TestTryCatch getTryCatch() {
    return findChildByClass(TestTryCatch.class);
  }

  @Override
  @Nullable
  public TestUseStmt getUseStmt() {
    return findChildByClass(TestUseStmt.class);
  }

  @Override
  @Nullable
  public TestVarDec getVarDec() {
    return findChildByClass(TestVarDec.class);
  }

  @Override
  @Nullable
  public TestWhileDo getWhileDo() {
    return findChildByClass(TestWhileDo.class);
  }

}

// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.tscript.ide.psi.MixinElements.Operation;
import com.tscript.ide.psi.MixinElements.Closure;
import com.tscript.ide.psi.MixinElements.IdentifierDef;
import com.tscript.ide.psi.MixinElements.Visibility;
import com.tscript.ide.psi.MixinElements.TestNSpaceDef;
import com.tscript.ide.psi.MixinElements.VariableDef;
import com.tscript.ide.psi.MixinElements.ParameterDef;
import com.tscript.ide.psi.MixinElements.ForLoop;
import com.tscript.ide.psi.MixinElements.TryCatch;
import com.tscript.ide.psi.MixinElements.StaticAccessor;
import com.tscript.ide.psi.MixinElements.SuperMemAccess;

public class TestVisitor extends PsiElementVisitor {

  public void visitAndExpr(@NotNull TestAndExpr o) {
    visitExpr(o);
  }

  public void visitArg(@NotNull TestArg o) {
    visitPsiElement(o);
  }

  public void visitArgList(@NotNull TestArgList o) {
    visitPsiElement(o);
  }

  public void visitArrayExpr(@NotNull TestArrayExpr o) {
    visitExpr(o);
  }

  public void visitAssignExpr(@NotNull TestAssignExpr o) {
    visitExpr(o);
  }

  public void visitAssignOp(@NotNull TestAssignOp o) {
    visitOperation(o);
  }

  public void visitBlock(@NotNull TestBlock o) {
    visitPsiElement(o);
  }

  public void visitBoolExpr(@NotNull TestBoolExpr o) {
    visitExpr(o);
  }

  public void visitBreakStmt(@NotNull TestBreakStmt o) {
    visitPsiElement(o);
  }

  public void visitCall(@NotNull TestCall o) {
    visitPsiElement(o);
  }

  public void visitChainableIdentifier(@NotNull TestChainableIdentifier o) {
    visitPsiElement(o);
  }

  public void visitClassBodyDef(@NotNull TestClassBodyDef o) {
    visitPsiElement(o);
  }

  public void visitClassDef(@NotNull TestClassDef o) {
    visitPsiElement(o);
  }

  public void visitClosure(@NotNull TestClosure o) {
    visitPsiElement(o);
  }

  public void visitCompExpr(@NotNull TestCompExpr o) {
    visitExpr(o);
  }

  public void visitCompOp(@NotNull TestCompOp o) {
    visitOperation(o);
  }

  public void visitConstDec(@NotNull TestConstDec o) {
    visitStaticAccessor(o);
  }

  public void visitConstructorDef(@NotNull TestConstructorDef o) {
    visitPsiElement(o);
  }

  public void visitContainerAccess(@NotNull TestContainerAccess o) {
    visitPsiElement(o);
  }

  public void visitContinueStmt(@NotNull TestContinueStmt o) {
    visitPsiElement(o);
  }

  public void visitDefinition(@NotNull TestDefinition o) {
    visitPsiElement(o);
  }

  public void visitDictionaryContent(@NotNull TestDictionaryContent o) {
    visitPsiElement(o);
  }

  public void visitDictionaryEntry(@NotNull TestDictionaryEntry o) {
    visitPsiElement(o);
  }

  public void visitDictionaryExpr(@NotNull TestDictionaryExpr o) {
    visitExpr(o);
  }

  public void visitDoWhile(@NotNull TestDoWhile o) {
    visitPsiElement(o);
  }

  public void visitEqExpr(@NotNull TestEqExpr o) {
    visitExpr(o);
  }

  public void visitEqOp(@NotNull TestEqOp o) {
    visitOperation(o);
  }

  public void visitExpr(@NotNull TestExpr o) {
    visitPsiElement(o);
  }

  public void visitForLoop(@NotNull TestForLoop o) {
    visitPsiElement(o);
  }

  public void visitFromImport(@NotNull TestFromImport o) {
    visitPsiElement(o);
  }

  public void visitFromUse(@NotNull TestFromUse o) {
    visitPsiElement(o);
  }

  public void visitFunctionDef(@NotNull TestFunctionDef o) {
    visitPsiElement(o);
  }

  public void visitIdentifier(@NotNull TestIdentifier o) {
    visitIdentifierDef(o);
  }

  public void visitIfElse(@NotNull TestIfElse o) {
    visitPsiElement(o);
  }

  public void visitImportStmt(@NotNull TestImportStmt o) {
    visitPsiElement(o);
  }

  public void visitIntegerExpr(@NotNull TestIntegerExpr o) {
    visitExpr(o);
  }

  public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
    visitExpr(o);
  }

  public void visitMemAccess(@NotNull TestMemAccess o) {
    visitIdentifierDef(o);
  }

  public void visitMulExpr(@NotNull TestMulExpr o) {
    visitExpr(o);
  }

  public void visitMulOp(@NotNull TestMulOp o) {
    visitOperation(o);
  }

  public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
    visitNSpaceDef(o);
  }

  public void visitNegationExpr(@NotNull TestNegationExpr o) {
    visitExpr(o);
  }

  public void visitNotExpr(@NotNull TestNotExpr o) {
    visitExpr(o);
  }

  public void visitNullExpr(@NotNull TestNullExpr o) {
    visitExpr(o);
  }

  public void visitOrExpr(@NotNull TestOrExpr o) {
    visitExpr(o);
  }

  public void visitParam(@NotNull TestParam o) {
    visitParameterDef(o);
  }

  public void visitPlusExpr(@NotNull TestPlusExpr o) {
    visitExpr(o);
  }

  public void visitPlusOp(@NotNull TestPlusOp o) {
    visitOperation(o);
  }

  public void visitPowExpr(@NotNull TestPowExpr o) {
    visitExpr(o);
  }

  public void visitRangeExpr(@NotNull TestRangeExpr o) {
    visitExpr(o);
  }

  public void visitRealExpr(@NotNull TestRealExpr o) {
    visitExpr(o);
  }

  public void visitReturnStmt(@NotNull TestReturnStmt o) {
    visitPsiElement(o);
  }

  public void visitShiftExpr(@NotNull TestShiftExpr o) {
    visitExpr(o);
  }

  public void visitShiftOp(@NotNull TestShiftOp o) {
    visitOperation(o);
  }

  public void visitSingleConst(@NotNull TestSingleConst o) {
    visitVariableDef(o);
  }

  public void visitSingleVar(@NotNull TestSingleVar o) {
    visitVariableDef(o);
  }

  public void visitStmt(@NotNull TestStmt o) {
    visitPsiElement(o);
  }

  public void visitStringExpr(@NotNull TestStringExpr o) {
    visitExpr(o);
  }

  public void visitSuperAccess(@NotNull TestSuperAccess o) {
    visitSuperMemAccess(o);
  }

  public void visitThisExpr(@NotNull TestThisExpr o) {
    visitExpr(o);
  }

  public void visitThrowStmt(@NotNull TestThrowStmt o) {
    visitPsiElement(o);
  }

  public void visitTryCatch(@NotNull TestTryCatch o) {
    visitPsiElement(o);
  }

  public void visitTypeofPrefixExpr(@NotNull TestTypeofPrefixExpr o) {
    visitExpr(o);
  }

  public void visitUnaryExpr(@NotNull TestUnaryExpr o) {
    visitExpr(o);
  }

  public void visitUseStmt(@NotNull TestUseStmt o) {
    visitPsiElement(o);
  }

  public void visitVarDec(@NotNull TestVarDec o) {
    visitStaticAccessor(o);
  }

  public void visitVisibility(@NotNull TestVisibility o) {
    visitPsiElement(o);
  }

  public void visitWhileDo(@NotNull TestWhileDo o) {
    visitPsiElement(o);
  }

  public void visitXorExpr(@NotNull TestXorExpr o) {
    visitExpr(o);
  }

  public void visitIdentifierDef(@NotNull IdentifierDef o) {
    visitElement(o);
  }

  public void visitOperation(@NotNull Operation o) {
    visitElement(o);
  }

  public void visitParameterDef(@NotNull ParameterDef o) {
    visitElement(o);
  }

  public void visitStaticAccessor(@NotNull StaticAccessor o) {
    visitElement(o);
  }

  public void visitSuperMemAccess(@NotNull SuperMemAccess o) {
    visitElement(o);
  }

  public void visitNSpaceDef(@NotNull TestNSpaceDef o) {
    visitPsiElement(o);
  }

  public void visitVariableDef(@NotNull VariableDef o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}

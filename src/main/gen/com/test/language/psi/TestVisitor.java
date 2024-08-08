// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.test.language.psi.TestNamedElements.IdentifierDef;
import com.test.language.psi.TestNamedElements.TestNSpaceDef;
import com.test.language.psi.TestNamedElements.VariableDef;
import com.test.language.psi.TestNamedElements.ParameterDef;
import com.test.language.psi.TestNamedElements.SuperMemAccess;

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

  public void visitClassDef(@NotNull TestClassDef o) {
    visitClassDef(o);
  }

  public void visitClosure(@NotNull TestClosure o) {
    visitPsiElement(o);
  }

  public void visitClosures(@NotNull TestClosures o) {
    visitPsiElement(o);
  }

  public void visitCompExpr(@NotNull TestCompExpr o) {
    visitExpr(o);
  }

  public void visitCompOp(@NotNull TestCompOp o) {
    visitPsiElement(o);
  }

  public void visitConstDec(@NotNull TestConstDec o) {
    visitPsiElement(o);
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

  public void visitExpr(@NotNull TestExpr o) {
    visitPsiElement(o);
  }

  public void visitForLoop(@NotNull TestForLoop o) {
    visitPsiElement(o);
  }

  public void visitFunctionDef(@NotNull TestFunctionDef o) {
    visitFunctionDef(o);
  }

  public void visitIdentifier(@NotNull TestIdentifier o) {
    visitIdentifierDef(o);
  }

  public void visitIfElse(@NotNull TestIfElse o) {
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
    visitPsiElement(o);
  }

  public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
    visitNSpaceDef(o);
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
    visitPsiElement(o);
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
    visitPsiElement(o);
  }

  public void visitSingleConst(@NotNull TestSingleConst o) {
    visitVariableDef(o);
  }

  public void visitSingleVar(@NotNull TestSingleVar o) {
    visitVariableDef(o);
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

  public void visitVarDec(@NotNull TestVarDec o) {
    visitPsiElement(o);
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

  public void visitParameterDef(@NotNull ParameterDef o) {
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

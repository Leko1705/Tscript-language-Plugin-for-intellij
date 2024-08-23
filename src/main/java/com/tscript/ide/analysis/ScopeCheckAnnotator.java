package com.tscript.ide.analysis;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.tscript.ide.analysis.fixes.RemoveTextFix;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;

public class ScopeCheckAnnotator extends TestVisitor implements Annotator {

    private AnnotationHolder holder;
    private boolean inBlock = false;
    private boolean inClass = false;
    private boolean inLoop = false;
    private boolean inFunction = false;
    private boolean inLambda = false;
    private boolean inStaticFunction = false;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiFile){
            this.holder = holder;
            element.accept(this);
        }
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {
        element.acceptChildren(this);
    }

    @Override
    public void visitPsiElement(@NotNull PsiElement o) {
        o.acceptChildren(this);
    }

    private void checkStatic(PsiElement staticElement){
        if (staticElement != null && !inClass){
            LazyAnnotationBuilder.errorAnnotator(holder, staticElement, true, "Cannot use 'static' out of class")
                    .addLocalQuickFix(new RemoveTextFix("remove keyword 'super'"))
                    .create();
        }
    }

    @Override
    public void visitClassDef(@NotNull TestClassDef o) {
        checkStatic(o.getStaticElement());
        boolean inClassTemp = this.inClass;
        this.inClass = true;
        o.acceptChildren(this);
        this.inClass = inClassTemp;
    }

    @Override
    public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
        checkStatic(o.getStaticElement());
        boolean inClassTemp = this.inClass;
        this.inClass = false;
        o.acceptChildren(this);
        this.inClass = inClassTemp;
    }

    @Override
    public void visitBlock(@NotNull TestBlock o) {
        boolean inClassTemp = this.inBlock;
        this.inBlock = true;
        o.acceptChildren(this);
        this.inBlock = inClassTemp;
    }

    @Override
    public void visitFunctionDef(@NotNull TestFunctionDef o) {
        checkStatic(o.getStaticElement());

        if (o.getAbstractElement() != null && !inClass && !inFunction){
            LazyAnnotationBuilder.errorAnnotator(holder, o.getAbstractElement(), true, "Cannot define abstract function out of class")
                    .addLocalQuickFix(new RemoveTextFix("remove keyword 'abstract'"))
                    .create();
        }

        boolean inFunctionTemp = this.inFunction;
        this.inFunction = true;
        boolean inStaticTemp = this.inStaticFunction;
        this.inStaticFunction = o.getStaticElement() != null;
        o.acceptChildren(this);
        this.inFunction = inFunctionTemp;
        this.inStaticFunction = inStaticTemp;
    }

    @Override
    public void visitThisExpr(@NotNull TestThisExpr o) {
        if (!inLambda) {
            if (!inClass) {
                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'this' out of class").create();
            }
            else if (inStaticFunction) {
                LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'this' from a static context").create();
            }
        }
    }

    @Override
    public void visitSuperAccess(@NotNull TestSuperAccess o) {
        if (!inClass){
            LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'super' out of class").create();
        }
        else if (inStaticFunction){
            LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'super' from a static context").create();
        }
    }

    @Override
    public void visitUseStmt(@NotNull TestUseStmt o) {
        if (!inLambda && !inClass && !inFunction && !inLoop && !inStaticFunction && !inBlock){
            LazyAnnotationBuilder.warningAnnotation(holder, o, "usage of keyword 'use' in global scope hides potential not-defined-errors").create();
        }
    }

    @Override
    public void visitFromUse(@NotNull TestFromUse o) {
        if (!inLambda && !inClass && !inFunction && !inLoop && !inStaticFunction && !inBlock){
            LazyAnnotationBuilder.warningAnnotation(holder, o, "usage of keyword 'use' in global scope hides potential not-defined-errors").create();
        }
    }

    @Override
    public void visitWhileDo(@NotNull TestWhileDo o) {
        boolean inLoopTemp = this.inLoop;
        this.inLoop = true;
        o.acceptChildren(this);
        this.inLoop = inLoopTemp;
    }

    @Override
    public void visitDoWhile(@NotNull TestDoWhile o) {
        boolean inLoopTemp = this.inLoop;
        this.inLoop = true;
        o.acceptChildren(this);
        this.inLoop = inLoopTemp;
    }

    @Override
    public void visitForLoop(@NotNull TestForLoop o) {
        boolean inLoopTemp = this.inLoop;
        this.inLoop = true;
        o.acceptChildren(this);
        this.inLoop = inLoopTemp;
    }

    @Override
    public void visitBreakStmt(@NotNull TestBreakStmt o) {
        if (!inLoop){
            LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'break' out of loop")
                    .addLocalQuickFix(new RemoveTextFix("remove statement"))
                    .create();
        }
    }

    @Override
    public void visitContinueStmt(@NotNull TestContinueStmt o) {
        if (!inLoop){
            LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'continue' out of loop")
                    .addLocalQuickFix(new RemoveTextFix("remove statement"))
                    .create();
        }
    }

    @Override
    public void visitReturnStmt(@NotNull TestReturnStmt o) {
        if (!inFunction && !inLambda){
            LazyAnnotationBuilder.errorAnnotator(holder, o, true, "Cannot use 'return' out of function")
                    .addLocalQuickFix(new RemoveTextFix("remove statement"))
                    .create();
        }
    }

    @Override
    public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
        boolean inLambdaTemp = this.inLambda;
        this.inLambda = true;
        boolean inClassTemp = this.inClass;
        this.inClass = false;
        boolean inFunctionTemp = this.inFunction;
        this.inFunction = false;
        boolean inLoopTemp = this.inLoop;
        this.inLoop = false;
        boolean inStaticFunctionTemp = this.inStaticFunction;
        this.inStaticFunction = false;
        o.acceptChildren(this);
        this.inLambda = inLambdaTemp;
        this.inClass = inClassTemp;
        this.inFunction = inFunctionTemp;
        this.inLoop = inLoopTemp;
        this.inStaticFunction = inStaticFunctionTemp;
    }

}

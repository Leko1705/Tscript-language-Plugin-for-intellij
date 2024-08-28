package com.tscript.ide.analysis.hints;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.tscript.ide.analysis.LazyAnnotationBuilder;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;

public class LoopingBehaviorDetector implements Annotator {

    private static class Stop extends RuntimeException {}

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        try {
            if (element instanceof TestWhileDo w && w.getStmt() != null){
                w.getStmt().accept(new Handler(holder, findElement(element, TestTypes.WHILE)));
            }
            else if (element instanceof TestDoWhile d && d.getStmt() != null){
                d.getStmt().accept(new Handler(holder, findElement(element, TestTypes.DO)));
            }
            else if (element instanceof TestForLoop f && f.getStmt() != null){
                f.getStmt().accept(new Handler(holder, findElement(element, TestTypes.FOR)));
            }
        }
        catch (Stop ignored){}
    }

    private PsiElement findElement(PsiElement element, IElementType type){
        ASTNode node = element.getNode().findChildByType(type);
        return (node != null) ? node.getPsi() : null;
    }

    private static class Handler extends TestVisitor {
        private boolean inBaseLoop = true;
        private final AnnotationHolder holder;
        private final PsiElement markElement;


        public Handler(AnnotationHolder holder, PsiElement markElement) {
            this.holder = holder;
            this.markElement = markElement;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitIfElse(@NotNull TestIfElse o) {
            boolean prev = inBaseLoop;
            inBaseLoop = false;
            super.visitIfElse(o);
            inBaseLoop = prev;
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            boolean prev = inBaseLoop;
            inBaseLoop = false;
            super.visitForLoop(o);
            inBaseLoop = prev;
        }

        @Override
        public void visitWhileDo(@NotNull TestWhileDo o) {
            boolean prev = inBaseLoop;
            inBaseLoop = false;
            super.visitWhileDo(o);
            inBaseLoop = prev;
        }

        @Override
        public void visitDoWhile(@NotNull TestDoWhile o) {
            boolean prev = inBaseLoop;
            inBaseLoop = false;
            super.visitDoWhile(o);
            inBaseLoop = prev;
        }

        @Override
        public void visitTryCatch(@NotNull TestTryCatch o) {
            boolean prev = inBaseLoop;
            inBaseLoop = false;
            super.visitTryCatch(o);
            inBaseLoop = prev;
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
        }

        @Override
        public void visitReturnStmt(@NotNull TestReturnStmt o) {
            if (inBaseLoop){
                LazyAnnotationBuilder.warningAnnotation(holder, markElement, "loop does not loop").create();
            }
            throw new Stop();
        }

        @Override
        public void visitBreakStmt(@NotNull TestBreakStmt o) {
            if (inBaseLoop){
                LazyAnnotationBuilder.warningAnnotation(holder, markElement, "loop does not loop").create();
            }
            throw new Stop();
        }

        @Override
        public void visitContinueStmt(@NotNull TestContinueStmt o) {
            throw new Stop();
        }
    }

}

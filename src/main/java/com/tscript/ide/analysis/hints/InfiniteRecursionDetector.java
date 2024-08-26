package com.tscript.ide.analysis.hints;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.LazyAnnotationBuilder;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;

public class InfiniteRecursionDetector implements Annotator {

    private static final String ERROR_MESSAGE = "Infinite recursive call will cause an error";

    private static class Stop extends RuntimeException {}

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        try {
            if (element instanceof TestFunctionDef f){
                if (f.getBlock() != null)
                    f.getBlock().accept(new Detector(f.getName(), holder));
            }
            else if (element instanceof TestLambdaExpr l){
                if (l.getBlock() != null)
                    l.getBlock().accept(new Detector(null, holder));
            }
        }
        catch (Stop ignored){}
    }

    private static class Detector extends TestVisitor {

        private final String name;
        boolean onFunctionLevel = true;
        boolean inTryBlock = false;
        private final AnnotationHolder holder;

        private Detector(String name, AnnotationHolder holder) {
            this.name = name;
            this.holder = holder;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitIfElse(@NotNull TestIfElse o) {
            if (o.getExpr() != null)
                o.getExpr().acceptChildren(this);

            boolean prev = this.onFunctionLevel;
            this.onFunctionLevel = false;
            for (TestStmt stmt : o.getStmtList()){
                stmt.accept(this);
            }
            this.onFunctionLevel = prev;
        }

        @Override
        public void visitWhileDo(@NotNull TestWhileDo o) {
            handleLoop(o.getExpr(), o.getStmt());
        }

        @Override
        public void visitForLoop(@NotNull TestForLoop o) {
            handleLoop(o.getExpr(), o.getStmt());
        }

        private void handleLoop(TestExpr expr, TestStmt stmt) {
            if (expr != null)
                expr.acceptChildren(this);

            if (stmt != null) {
                boolean prev = this.onFunctionLevel;
                this.onFunctionLevel = false;
                stmt.accept(this);
                this.onFunctionLevel = prev;
            }
        }

        @Override
        public void visitDoWhile(@NotNull TestDoWhile o) {
            // we ignore this loop type, since its body is executed at least once
            super.visitDoWhile(o);
        }

        @Override
        public void visitReturnStmt(@NotNull TestReturnStmt o) {
            // exit point might avoid infinite recursion
            throw new Stop();
        }

        @Override
        public void visitThrowStmt(@NotNull TestThrowStmt o) {
            // exit point might avoid infinite recursion
            if (!inTryBlock) throw new Stop();
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            // ignore unnecessary calculation
        }

        @Override
        public void visitThisExpr(@NotNull TestThisExpr o) {
            if (name == null && onFunctionLevel && o.getNextSibling() instanceof TestCall){
                LazyAnnotationBuilder.warningAnnotation(holder, o, ERROR_MESSAGE).create();
            }
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            if (name != null && o.getName() != null && name.equals(o.getName())){
                // prevent wrong recursion detection
                onFunctionLevel = false;
            }
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            if (name != null && o.getName() != null && name.equals(o.getName())){
                // prevent wrong recursion detection
                onFunctionLevel = false;
            }
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            if (onFunctionLevel && o.getName() != null && name.equals(o.getName())){
                LazyAnnotationBuilder.warningAnnotation(holder, o, ERROR_MESSAGE).create();
            }
        }
    }

}

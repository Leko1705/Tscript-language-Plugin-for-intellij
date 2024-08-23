package com.tscript.ide.analysis;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.fixes.RemoveTextFix;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlowCheckAnnotator extends TestVisitor implements Annotator {

    private AnnotationHolder holder;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        this.holder = holder;
        element.accept(this);
    }

    @Override
    public void visitBlock(@NotNull TestBlock o) {
        List<TestStmt> children = o.getStmtList();

        outer:
        for (int i = 0; i < children.size(); i++) {
            TestStmt stmt = children.get(i);
            if (stmt.getReturnStmt() != null|| stmt.getThrowStmt() != null){
                if (i + 1 < children.size()){
                    LazyAnnotationBuilder.warningAnnotation(holder, children.get(i + 1), "unreachable statement")
                            .addLocalQuickFix(new RemoveTextFix("remove unreachable statement"))
                            .create();
                }
            }
            else if (stmt.getExpr() != null){
                TestExpr expr = stmt.getExpr();
                if (expr instanceof TestUnaryExpr unaryExpr) {
                    PsiElement[] unaryChildren = unaryExpr.getChildren();

                    for (PsiElement child : unaryChildren) {
                        if (child instanceof TestAssignExpr || child instanceof TestCall) {
                            continue outer;
                        }
                    }

                    LazyAnnotationBuilder.warningAnnotation(holder, children.get(i + 1), "expression statement has no effect and will be ignored during execution")
                            .addLocalQuickFix(new RemoveTextFix("remove useless expression statement", stmt))
                            .create();
                }
                else if (!(expr instanceof TestAssignExpr) && !(expr instanceof TestCall)){
                    LazyAnnotationBuilder.warningAnnotation(holder, stmt, "expression statement has no effect and will be ignored during execution")
                            .addLocalQuickFix(new RemoveTextFix("remove useless expression statement", stmt))
                            .create();
                }
            }
        }
    }

}

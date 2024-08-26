package com.tscript.ide.analysis.hints;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.tscript.ide.analysis.LazyAnnotationBuilder;
import com.tscript.ide.analysis.fixes.MultiFix;
import com.tscript.ide.analysis.fixes.RemoveTextFix;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;

public class EmptyIfBodyDetector implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TestIfElse ifElse){
            if (ifElse.getStmtList().isEmpty()) return;
            boolean thenPresent = isPresent(ifElse.getStmtList().get(0));
            if (ifElse.getStmtList().size() > 1) {
                boolean elsePresent = isPresent(ifElse.getStmtList().get(1));

                if (!thenPresent && elsePresent) {
                    LazyAnnotationBuilder.warningAnnotation(holder, ifElse.getFirstChild(), "empty if body")
                            .create();
                }
                else if (thenPresent && !elsePresent) {
                    PsiElement elseToken = findElseElement(ifElse);
                    if (elseToken != null) {
                        LazyAnnotationBuilder.warningAnnotation(holder, elseToken, "empty else body")
                                .addLocalQuickFix(new MultiFix(
                                        "remove else statement",
                                        new RemoveTextFix("", ifElse.getStmtList().get(1)),
                                        new RemoveTextFix("", elseToken)))
                                .create();
                    }
                }
                else if (!thenPresent /* && !elsePresent (implicit) */) {
                    LazyAnnotationBuilder.warningAnnotation(holder, ifElse.getFirstChild(), "empty if and else body")
                            .addLocalQuickFix(new RemoveTextFix("remove if-else statement", ifElse))
                            .create();
                }
            }
            else {
                if (!thenPresent) {
                    LazyAnnotationBuilder.warningAnnotation(holder, ifElse.getFirstChild(), "empty if body")
                            .addLocalQuickFix(new RemoveTextFix("remove if statement", ifElse))
                            .create();
                }
            }
        }
    }

    // returns true if stmt is a non-empty block comment
    private boolean isPresent(TestStmt stmt){
        if (stmt.getBlock() == null) return true;

        for (PsiElement element : stmt.getBlock().getChildren()) {
            if (element instanceof LeafPsiElement) continue;
            return true;
        }
        return false;
    }

    private PsiElement findElseElement(PsiElement element){
        ASTNode node = element.getNode().findChildByType(TestTypes.ELSE);
        return (node != null) ? node.getPsi() : null;
    }

}

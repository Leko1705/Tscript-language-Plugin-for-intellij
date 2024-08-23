package com.tscript.ide.formatting;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.tscript.ide.psi.*;
import com.tscript.ide.psi.TestBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TscriptFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root,
                                                          @NotNull Document document,
                                                          boolean quick) {

        List<FoldingDescriptor> descriptors = new ArrayList<>();

        root.accept(new TestVisitor(){

            private List<PsiElement> importList = new ArrayList<>();

            private void stopImportFoldCheck() {
                if (importList == null) return;

                if (importList.isEmpty()) {
                    importList = null;
                    return;
                }

                descriptors.add(
                        new FoldingDescriptor(
                                importList.get(0).getNode(),
                                new TextRange(importList.get(0).getTextRange().getStartOffset(), importList.get(importList.size()-1).getTextRange().getEndOffset()),
                                FoldingGroup.newGroup("block"),
                                new HashSet<>(importList)));

                importList = null;
            }

            @Override
            public void visitFile(@NotNull PsiFile file) {
                file.acceptChildren(this);
                stopImportFoldCheck();
            }

            @Override
            public void visitImportStmt(@NotNull TestImportStmt o) {
                if (importList == null) return;
                importList.add(o);
            }

            @Override
            public void visitFromImport(@NotNull TestFromImport o) {
                if (importList == null) return;
                importList.add(o);
            }

            @Override
            public void visitElement(@NotNull PsiElement element) {
                stopImportFoldCheck();
                element.acceptChildren(this);
            }

            @Override
            public void visitWhiteSpace(@NotNull PsiWhiteSpace space) {
            }

            @Override
            public void visitStmt(@NotNull TestStmt o) {
                o.acceptChildren(this);
            }

            @Override
            public void visitBlock(@NotNull TestBlock o) {
                o.acceptChildren(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getNode(),
                                o.getTextRange(),
                                FoldingGroup.newGroup("block"),
                                Collections.singleton(o)));
            }

            @Override
            public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                stopImportFoldCheck();
                o.acceptChildren(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getNode(),
                                o.getTextRange(),
                                FoldingGroup.newGroup("nspace"),
                                Collections.singleton(o)));
            }

            @Override
            public void visitClassDef(@NotNull TestClassDef o) {
                stopImportFoldCheck();
                if (o.getClassBodyDef() == null) return;
                o.getClassBodyDef().accept(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getClassBodyDef().getNode(),
                                o.getClassBodyDef().getTextRange(),
                                FoldingGroup.newGroup("class"),
                                Collections.singleton(o.getClassBodyDef())));
            }

            @Override
            public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
                stopImportFoldCheck();
                if (o.getBlock() != null)
                    o.getBlock().acceptChildren(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getNode(),
                                o.getTextRange(),
                                FoldingGroup.newGroup("lambda"),
                                Collections.singleton(o)));
            }
        });

        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        if (element == null) return null;

        if (element instanceof TestBlock
                || element instanceof TestClassBodyDef
                || element instanceof TestNamespaceDef) return "{...}";

        else if (element instanceof TestLambdaExpr lambdaExpr){
            StringBuilder sb = new StringBuilder("function");
            Iterator<TestClosure> closureItr = lambdaExpr.getClosureList().iterator();
            if (closureItr.hasNext()) {
                sb.append("[").append(closureItr.next().getName());
                while (closureItr.hasNext()){
                    TestClosure closure = closureItr.next();
                    if (closure.getName() == null) continue;
                    sb.append(", ").append(closure.getName());
                }
                sb.append("]");
            }

            sb.append("(");
            Iterator<TestParam> paramItr = lambdaExpr.getParamList().iterator();
            if (paramItr.hasNext()) {
                sb.append(paramItr.next().getName());
                while (paramItr.hasNext()){
                    TestParam param = paramItr.next();
                    if (param.getName() == null) continue;
                    sb.append(", ").append(param.getName());
                }
            }
            sb.append("){...}");
            return sb.toString();
        }

        else if (element instanceof TestImport || element instanceof TestFromImport)
            return StringUtil.THREE_DOTS;

        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        return element instanceof TestLambdaExpr
                || element instanceof TestImport
                || element instanceof TestFromImport;
    }
}

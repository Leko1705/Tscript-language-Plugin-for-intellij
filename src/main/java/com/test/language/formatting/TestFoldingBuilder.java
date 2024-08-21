package com.test.language.formatting;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.test.language.psi.*;
import com.test.language.psi.TestBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TestFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root,
                                                          @NotNull Document document,
                                                          boolean quick) {

        List<FoldingDescriptor> descriptors = new ArrayList<>();

        root.accept(new TestVisitor(){
            @Override
            public void visitElement(@NotNull PsiElement element) {
                element.acceptChildren(this);
            }

            @Override
            public void visitBlock(@NotNull TestBlock o) {
                o.acceptChildren(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getNode(),
                                new TextRange(o.getTextRange().getStartOffset(), o.getTextRange().getEndOffset()),
                                FoldingGroup.newGroup("block"),
                                Collections.singleton(o)));
            }

            @Override
            public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                o.acceptChildren(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getNode(),
                                new TextRange(o.getTextRange().getStartOffset(), o.getTextRange().getEndOffset()),
                                FoldingGroup.newGroup("nspace"),
                                Collections.singleton(o)));
            }

            @Override
            public void visitClassDef(@NotNull TestClassDef o) {
                o.acceptChildren(this);
                if (o.getClassBodyDef() == null) return;
                descriptors.add(
                        new FoldingDescriptor(
                                o.getClassBodyDef().getNode(),
                                new TextRange(o.getClassBodyDef().getTextRange().getStartOffset()-1, o.getClassBodyDef().getTextRange().getEndOffset()+1),
                                FoldingGroup.newGroup("class"),
                                Collections.singleton(o.getClassBodyDef())));
            }

            @Override
            public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
                if (o.getBlock() != null)
                    o.getBlock().acceptChildren(this);
                descriptors.add(
                        new FoldingDescriptor(
                                o.getNode(),
                                new TextRange(o.getTextRange().getStartOffset(), o.getTextRange().getEndOffset()),
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

        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        return element instanceof TestLambdaExpr;
    }
}

package com.tscript.ide.reference;

import com.intellij.navigation.DirectNavigationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TscriptDirectNavigationProvider implements DirectNavigationProvider {

    private static class Stop extends RuntimeException {
        public final PsiElement resolved;
        public Stop(PsiElement resolved) {
            this.resolved = resolved;
        }
    }

    @Override
    public @Nullable PsiElement getNavigationElement(@NotNull PsiElement element) {
        ResolveAction action = new ResolveAction();
        element.accept(action);
        return action.resolved;
    }


    private class ResolveAction extends TestVisitor {

        private PsiElement resolved;

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            if (o.getParent() instanceof TestChainableIdentifier cIdent) {
                resolved = resolveSuperClass(cIdent);
            }
            else if (o.getName() != null){
                UpwardsSearcher searcher = new UpwardsSearcher(o.getName());
                try {
                    o.getParent().accept(searcher);
                } catch (Stop stop){
                    resolved = stop.resolved;
                }
            }
        }
    }

    private PsiElement resolveSuperClass(@NotNull TestChainableIdentifier cIdent) {
        List<TestIdentifier> list = new ArrayList<>();
        for (TestIdentifier child : cIdent.getIdentifierList()){
            list.add(child);
            if (child == cIdent) break;
        }
        return TscriptASTUtils.resolve(cIdent.getContainingFile(), list);
    }


    private static class UpwardsSearcher extends TestVisitor {

        private final String searched;

        private UpwardsSearcher(String searched) {
            this.searched = searched;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.getParent().accept(this);
        }

        @Override
        public void visitWhiteSpace(@NotNull PsiWhiteSpace o) {
            if (o.getPrevSibling() != null){
                o.getPrevSibling().accept(this);
            }
            else {
                o.getParent().accept(this);
            }
        }

        @Override
        public void visitStmt(@NotNull TestStmt o) {
            if (o.getVarDec() != null){
                TestVarDec varDecTree = o.getVarDec();
                for (TestSingleVar singleVar : varDecTree.getSingleVarList()){
                    if (searched.equals(singleVar.getName())){
                        throw new Stop(singleVar.getNameIdentifier());
                    }
                }
            }
            else if (o.getConstDec() != null){
                TestConstDec varDecTree = o.getConstDec();
                for (TestSingleConst singleVar : varDecTree.getSingleConstList()){
                    if (searched.equals(singleVar.getName())){
                        throw new Stop(singleVar.getNameIdentifier());
                    }
                }
            }

            if (o.getPrevSibling() != null){
                o.getPrevSibling().accept(this);
            }
            else {
                o.getParent().accept(this);
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }

            for (TestParam param : o.getParamList()){
                if (searched.equals(param.getName())){
                    throw new Stop(param.getNameIdentifier());
                }
            }

            o.getParent().accept(this);
        }

        @Override
        public void visitLambdaExpr(@NotNull TestLambdaExpr o) {
            for (TestClosure closure : o.getClosureList()){
                if (searched.equals(closure.getName())) {
                    throw new Stop(closure.getNameIdentifier());
                }
            }

            for (TestParam param : o.getParamList()){
                if (searched.equals(param.getName())){
                    throw new Stop(param.getNameIdentifier());
                }
            }

            o.getContainingFile().accept(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }

            if (o.getClassBodyDef() != null)
                o.getClassBodyDef().acceptChildren(new LevelSearcher(searched));

            TestClassDef superClass = TscriptASTUtils.getSuperClass(o);
            if (superClass != null){
                superClass.accept(this);
            }
            else {
                o.getParent().accept(this);
            }
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }
            o.acceptChildren(new LevelSearcher(searched));
            o.getParent().accept(this);
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            file.acceptChildren(new LevelSearcher(searched));
        }
    }


    private static class LevelSearcher extends TestVisitor {

        private final String searched;

        private LevelSearcher(String searched) {
            this.searched = searched;
        }

        @Override
        public void visitDefinition(@NotNull TestDefinition o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }
        }

        @Override
        public void visitVarDec(@NotNull TestVarDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitStmt(@NotNull TestStmt o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            if (searched.equals(o.getName())){
                throw new Stop(o.getNameIdentifier());
            }
        }
    }

}

package com.tscript.ide.psi;

import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class TscriptASTUtils {

    private static class CancelProcessException extends RuntimeException {}


    private TscriptASTUtils() {}


    public static TestClassDef getCurrentClass(@NotNull PsiElement element){
        if (element instanceof TestClassDef e) return e;

        PsiElement current = element.getParent();
        while (current != null && !(current instanceof TestClassDef) && !(current instanceof TestLambdaExpr))
            current = current.getParent();

        if (current == null || current instanceof TestLambdaExpr)
            return null;

        return (TestClassDef) current;
    }



    public static TestClassDef getSuperClass(@NotNull TestClassDef classTree){
        if (classTree.getSuper() == null) return null;
        PsiElement resolved = resolve(classTree.getSuper());
        return (resolved instanceof TestClassDef)
                ? (TestClassDef) resolved
                : null;
    }


    public static PsiElement resolve(TestChainableIdentifier chainableIdentifier){
        if (chainableIdentifier == null) return null;
        return resolve(chainableIdentifier.getContainingFile(), chainableIdentifier.getIdentifierList());
    }

    public static PsiElement resolve(@NotNull PsiFile root, @NotNull List<TestIdentifier> chainableIdentifierList){

        List<String> accessChain = new ArrayList<>();
        for (TestIdentifier access : chainableIdentifierList){
            if (access.getName() == null) return null;
            accessChain.add(access.getName());
        }

        Ref<PsiElement> found = new Ref<>(null);
        Iterator<String> accessItr = accessChain.iterator();
        if (!accessItr.hasNext()) return null;

        final Ref<String> next = new Ref<>(accessItr.next());

        try {
            root.accept(new TestVisitor(){

                @Override
                public void visitFile(@NotNull PsiFile file) {
                    file.acceptChildren(this);
                }

                @Override
                public void visitDefinition(@NotNull TestDefinition o) {
                    o.acceptChildren(this);
                }

                @Override
                public void visitClassDef(@NotNull TestClassDef o) {

                    if (o.getName() == null || !o.getName().equals(next.get()) || o.getClassBodyDef() == null) return;

                    if (!accessItr.hasNext()) {
                        found.set(o);
                        throw new CancelProcessException();
                    }

                    next.set(accessItr.next());
                    for (TestDefinition nestedClass : o.getClassBodyDef().getDefinitionList()){
                        nestedClass.accept(this);
                    }
                }

                @Override
                public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                    if (o.getName() == null || !o.getName().equals(next.get())) return;

                    if (!accessItr.hasNext()) {
                        found.set(o);
                        throw new CancelProcessException();
                    }

                    next.set(accessItr.next());
                    for (TestDefinition nestedClass : o.getDefinitionList()){
                        nestedClass.accept(this);
                    }
                }
            });
        }
        catch (CancelProcessException ignored){}

        return found.get();
    }

}

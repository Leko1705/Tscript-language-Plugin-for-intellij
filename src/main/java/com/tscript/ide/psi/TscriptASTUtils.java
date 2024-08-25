package com.tscript.ide.psi;

import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.tscript.ide.highlight.TscriptLineMarkerProvider;
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

    public static PsiElement resolve(PsiFile root, String chain){
        String[] split = chain.split("\\.");
        if (split.length == 0) return null;
        return resolve(root, List.of(split));
    }

    public static PsiElement resolve(@NotNull PsiFile root, @NotNull List<TestIdentifier> chainableIdentifierList){
        List<String> accessChain = new ArrayList<>();
        for (TestIdentifier access : chainableIdentifierList){
            if (access.getName() == null) return null;
            accessChain.add(access.getName());
        }
        return resolve(root, accessChain);
    }

    public static PsiElement resolve(@NotNull PsiFile root, @NotNull Iterable<String> path){
        Ref<PsiElement> found = new Ref<>(null);
        Iterator<String> accessItr = path.iterator();
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

                @Override
                public void visitFunctionDef(@NotNull TestFunctionDef o) {
                    if (o.getName() == null || !o.getName().equals(next.get())) return;

                    if (!accessItr.hasNext()) {
                        found.set(o);
                        throw new CancelProcessException();
                    }
                }
            });
        }
        catch (CancelProcessException ignored){}

        return found.get();
    }


    public static TestVisibility resolveVisibility(PsiElement element) {
        Ref<TestVisibility> visibility = new Ref<>(null);

        try {
            element.accept(new TestVisitor(){

                private PsiElement prev;

                @Override
                public void visitElement(@NotNull PsiElement element) {
                    prev = element;
                    if (element.getParent() != null)
                        element.getParent().accept(this);
                }

                @Override
                public void visitClassBodyDef(@NotNull TestClassBodyDef o) {
                    for (PsiElement child : o.getChildren()){
                        if (child instanceof TestVisibility v){
                            visibility.set(v);
                        }
                        else if (child == prev)
                            throw new CancelProcessException();
                    }
                }
            });
        }
        catch (CancelProcessException ignored){
        }

        return visibility.get();
    }


    public static TestFunctionDef getOverriddenFunction(@NotNull TestFunctionDef functionTree){
        TestClassDef classDef = TscriptASTUtils.getCurrentClass(functionTree);

        if (classDef == null) return null;
        if (classDef.getSuper() == null) return null;

        TestClassDef superClass = TscriptASTUtils.getSuperClass(classDef);
        if (superClass == null || superClass.getClassBodyDef() == null) return null;

        Ref<TestFunctionDef> overriddenFunction = new Ref<>(null);

        try {
            superClass.getClassBodyDef().acceptChildren(new TestVisitor(){

                @Override
                public void visitDefinition(@NotNull TestDefinition o) {
                    o.acceptChildren(this);
                }

                public void visitFunctionDef(@NotNull TestFunctionDef o) {
                    if (o.getName() == null) return;
                    if (!o.getName().equals(functionTree.getName())) return;
                    overriddenFunction.set(o);
                    throw new CancelProcessException();
                }

            });
        }
        catch (CancelProcessException ignored){}

        return overriddenFunction.get();
    }


    public static String fullQualifiedName(@NotNull PsiElement element){
        StringBuilder builder = new StringBuilder();

        element.accept(new TestVisitor(){
            boolean leader = true;

            private void append(String text){
                if (!leader)
                    builder.insert(0, ".");
                builder.insert(0, text);
                leader = false;
            }

            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element.getParent() != null)
                    element.getParent().accept(this);
            }

            @Override
            public void visitFunctionDef(@NotNull TestFunctionDef o) {
                append(o.getName());
                o.getParent().accept(this);
            }

            @Override
            public void visitClassDef(@NotNull TestClassDef o) {
                append(o.getName());
                o.getParent().accept(this);
            }

            @Override
            public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
                append(o.getName());
                o.getParent().accept(this);
            }

            @Override
            public void visitSingleVar(@NotNull TestSingleVar o) {
                append(o.getName());
                o.getParent().accept(this);
            }

            @Override
            public void visitSingleConst(@NotNull TestSingleConst o) {
                append(o.getName());
                o.getParent().accept(this);
            }
        });

        return builder.toString();
    }

}

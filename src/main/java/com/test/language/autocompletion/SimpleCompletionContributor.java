package com.test.language.autocompletion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.test.language.psi.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;

final class SimpleCompletionContributor extends CompletionContributor {

    SimpleCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(TestTypes.IDENT),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                        PsiElement position = parameters.getPosition();

                        Set<NameInfo> names = collectNames(position);

                        for (NameInfo name : names) {
                            LookupElementBuilder builder = LookupElementBuilder.create(name.name);
                            if (name.icon != null){
                                builder = builder.withIcon(name.icon);
                            }
                            if (name.where != null){
                                builder = builder.withTypeText(name.where);
                            }
                            result.addElement(builder);
                        }
                    }
                });
    }


    private record NameInfo(@NotNull String name, Icon icon, String where) {

        public NameInfo(@NotNull String name, Icon icon){
            this(name, icon, null);
        }

    }


    private Set<NameInfo> collectNames(@NotNull PsiElement element){
        Set<NameInfo> names = new HashSet<>();
        Queue<List<String>> laterTasks = new ArrayDeque<>();
        UpwardsCollector collector = new UpwardsCollector(names, laterTasks);

        PsiElement curr = element;
        while (curr != null){
            curr.accept(collector);
            curr = curr.getParent();
        }

        PsiFile file = element.getContainingFile();
        for (List<String> accessChain : laterTasks){
            if (accessChain.isEmpty()) continue;
            file.acceptChildren(new LaterCollector(names, accessChain.iterator(), laterTasks));
        }

        return names;
    }

    private static class UpwardsCollector extends TestVisitor {
        private final Set<NameInfo> names;
        private final Queue<List<String>> laterTasks;

        private UpwardsCollector(Set<NameInfo> names, Queue<List<String>> laterTasks) {
            this.names = names;
            this.laterTasks = laterTasks;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            file.accept(new TopLevelCollector(names));
        }

        @Override
        public void visitStmt(@NotNull TestStmt o) {
            o.acceptChildren(this);
            if (o.getPrevSibling() != null)
                o.getPrevSibling().accept(this);
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Function));
            }
            for (TestParam param : o.getParamList()){
                param.accept(this);
            }
        }

        @Override
        public void visitParam(@NotNull TestParam o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Parameter));
            }
        }

        @Override
        public void visitVarDec(@NotNull TestVarDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Variable));
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Constant));
            }
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Class));
            }
            o.acceptChildren(new ClassLevelCollector(o.getName(), names, true));

            if (o.getSuper() != null){
                List<String> accessChain = new LinkedList<>();
                for (TestIdentifier ident : o.getSuper().getIdentifierList()){
                    if (ident.getName() != null)
                        accessChain.add(ident.getName());
                    else {
                        // stop procedure
                        // name is incomplete
                        accessChain = null;
                        break;
                    }
                }
                if (accessChain != null){
                    laterTasks.add(accessChain);
                }
            }
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Package));
            }
        }
    }


    private static class TopLevelCollector extends TestVisitor {
        private final Set<NameInfo> names;

        private TopLevelCollector(Set<NameInfo> names) {
            this.names = names;
        }

        @Override
        public void visitFile(@NotNull PsiFile file) {
            file.acceptChildren(this);
        }

        @Override
        public void visitStmt(@NotNull TestStmt o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitVarDec(@NotNull TestVarDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Variable));
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Constant));
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Function));
            }
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Class));
            }
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Package));
            }
        }
    }



    private static class ClassLevelCollector extends TestVisitor {
        private final String className;
        private final Set<NameInfo> names;
        private final boolean allowPrivates;
        private boolean inPrivateState = false;

        private ClassLevelCollector(String className, Set<NameInfo> names, boolean allowPrivates) {
            this.className = className;
            this.names = names;
            this.allowPrivates = allowPrivates;
        }

        @Override
        public void visitVisibility(@NotNull TestVisibility o) {
            if (o.getName() == null) return;
            inPrivateState = o.getName().equalsIgnoreCase("private");
        }

        @Override
        public void visitVarDec(@NotNull TestVarDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleVar(@NotNull TestSingleVar o) {
            if (!allowPrivates && inPrivateState) return;
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Field, className));
            }
        }

        @Override
        public void visitConstDec(@NotNull TestConstDec o) {
            o.acceptChildren(this);
        }

        @Override
        public void visitSingleConst(@NotNull TestSingleConst o) {
            if (!allowPrivates && inPrivateState) return;
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Constant, className));
            }
        }

        @Override
        public void visitFunctionDef(@NotNull TestFunctionDef o) {
            if (!allowPrivates && inPrivateState) return;
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Method, className));
            }
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (!allowPrivates && inPrivateState) return;
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Class, className));
            }
        }

        @Override
        public void visitNamespaceDef(@NotNull TestNamespaceDef o) {
            if (!allowPrivates && inPrivateState) return;
            if (o.getName() != null){
                names.add(new NameInfo(o.getName(), AllIcons.Nodes.Package, className));
            }
        }
    }


    private static class LaterCollector extends TestVisitor {
        private final Set<NameInfo> names;
        private final Queue<List<String>> laterTasks;
        private final Iterator<String> state;
        private String curr;

        private LaterCollector(Set<NameInfo> names, Iterator<String> state, Queue<List<String>> laterTasks) {
            this.names = names;
            this.laterTasks = laterTasks;
            this.state = state;
            this.curr = state.next();
        }

        @Override
        public void visitClassDef(@NotNull TestClassDef o) {
            if (o.getName() != null && o.getName().equals(curr)){
                if (state.hasNext()){
                    curr = state.next();
                    o.acceptChildren(this);
                }
                else {
                    o.acceptChildren(new ClassLevelCollector(o.getName(), names, false));

                    if (o.getSuper() != null){
                        List<String> accessChain = new LinkedList<>();
                        for (TestIdentifier ident : o.getSuper().getIdentifierList()){
                            if (ident.getName() != null)
                                accessChain.add(ident.getName());
                            else {
                                // stop procedure
                                // name is incomplete
                                accessChain = null;
                                break;
                            }
                        }
                        if (accessChain != null){
                            laterTasks.add(accessChain);
                        }
                    }
                }
            }
        }
    }

}
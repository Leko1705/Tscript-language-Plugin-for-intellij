package com.tscript.ide.analysis.symtab;

import com.intellij.psi.PsiElement;

import java.util.function.Function;

public class Table {
    public final Scope root;
    public Scope currentScope;
    public Table(Scope root) {
        this.root = root;
        currentScope = root;
    }
    public void enterScope(PsiElement element){
        currentScope = currentScope.children.get(element);
        if (currentScope == null)
            throw new AssertionError();
    }

    public void leaveScope(){
        currentScope = currentScope.parent;
    }

    public void moveTopLevel(){
        currentScope = root;
    }

    public boolean checkHierarchy(Function<Scope, ContinueAction> tester){
        Scope curr = currentScope;
        while (curr != null){
            ContinueAction action = tester.apply(curr);
            if (action == ContinueAction.SUCCESS){
                return true;
            }
            if (action == ContinueAction.STOP)
                return false;
            curr = curr.parent;
        }
        return false;
    }

    public Symbol search(Function<Scope, Symbol> tester){
        Scope scope = currentScope;
        do {
            Symbol sym = scope.search(tester);
            if (sym != null) return sym;
            scope = scope.parent;
        }while (scope != null);
        return null;
    }
}
package com.tscript.ide.analysis.symtab;

import com.intellij.psi.PsiElement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Scope {

    public Scope(Kind kind, Scope parent, PsiElement element) {
        this.kind = kind;
        this.parent = parent;
        this.psiElement = element;
    }

    public Scope(Kind kind, PsiElement element) {
        this(kind, null, element);
    }

    public enum Kind {
        BLOCK,
        GLOBAL,
        FUNCTION,
        LAMBDA,
        CONSTRUCTOR,
        CLASS,
        NAMESPACE
    }

    public final Kind kind;
    public final Scope parent;
    public final PsiElement psiElement;
    public final Map<String, Symbol> table = new HashMap<>();
    public Map<PsiElement, Scope> children = new HashMap<>();

    public Symbol search(Function<Scope, Symbol> tester){
        return tester.apply(this);
    }
}

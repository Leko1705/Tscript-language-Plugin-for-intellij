package com.tscript.ide.analysis.symtab;

import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.utils.Visibility;

public class Symbol {

    public Symbol(String name, Visibility visibility, PsiElement element, Kind kind, Scope.Kind where, boolean isStatic) {
        this.name = name;
        this.visibility = visibility;
        this.element = element;
        this.kind = kind;
        this.where = where;
        this.isStatic = isStatic;
    }

    public enum Kind {
        VARIABLE,
        CONSTANT,
        FUNCTION,
        CLASS,
        NAMESPACE,
        UNKNOWN
    }

    public final String name;
    public final Visibility visibility;
    public final PsiElement element;
    public final Kind kind;
    public final Scope.Kind where;
    public final boolean isStatic;

}

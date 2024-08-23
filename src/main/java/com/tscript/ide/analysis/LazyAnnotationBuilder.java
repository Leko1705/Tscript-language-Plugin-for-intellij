package com.tscript.ide.analysis;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.tscript.ide.highlight.Styles;
import com.tscript.ide.highlight.TscriptSyntaxHighlighter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LazyAnnotationBuilder {

    private final PsiElement psiElement;
    private AnnotationBuilder builder;
    private final Set<TextAttributesKey> styles = new HashSet<>();

    public LazyAnnotationBuilder(PsiElement psiElement, AnnotationBuilder builder){
        this.psiElement = psiElement;
        this.builder = builder;
    }

    public LazyAnnotationBuilder addLocalQuickFix(LocalQuickFix quickFix){
        builder = builder.newLocalQuickFix(quickFix, QuickFixDescriptionFactory.create(psiElement)).registerFix();
        return this;
    }

    public LazyAnnotationBuilder addTextStyles(Collection<TextAttributesKey> keys){
        styles.addAll(keys);
        return this;
    }

    public void create(){
        builder.textAttributes(Styles.mergeAttributes(styles.toArray(new TextAttributesKey[0]))).create();
    }


    public static LazyAnnotationBuilder errorAnnotator(AnnotationHolder holder, PsiElement element, boolean underline, String message) {
        AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
                .highlightType(ProblemHighlightType.ERROR)
                .range(element);

        LazyAnnotationBuilder lazyAnnotator = new LazyAnnotationBuilder(element, builder);

        if (underline) {
            lazyAnnotator.addTextStyles(Set.of(TscriptSyntaxHighlighter.ERROR_UNDERLINE));
        }
        else {
            lazyAnnotator.addTextStyles(Set.of(TscriptSyntaxHighlighter.ERROR_MARK));
        }

        return lazyAnnotator;
    }

    public static LazyAnnotationBuilder warningAnnotation(AnnotationHolder holder, PsiElement element, String message) {
        return new LazyAnnotationBuilder(element, holder.newAnnotation(HighlightSeverity.WARNING, message)
                .highlightType(ProblemHighlightType.WARNING)
                .range(element));
    }

    public static LazyAnnotationBuilder weakWarningAnnotation(AnnotationHolder holder, PsiElement element, String message) {
        return new LazyAnnotationBuilder(element, holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
                .highlightType(ProblemHighlightType.WEAK_WARNING)
                .range(element));
    }

    public static void setTextStyle(AnnotationHolder holder, PsiElement element, Collection<TextAttributesKey> styles) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(Styles.mergeAttributes(styles.toArray(new TextAttributesKey[0])))
                .create();
    }

}

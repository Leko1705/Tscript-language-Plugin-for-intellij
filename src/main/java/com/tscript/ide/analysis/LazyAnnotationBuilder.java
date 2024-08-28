package com.tscript.ide.analysis;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.tscript.ide.analysis.fixes.QuickFixDescriptorFactory;
import com.tscript.ide.highlight.Styles;
import com.tscript.ide.highlight.TscriptSyntaxHighlighter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface LazyAnnotationBuilder {

    LazyAnnotationBuilder EMPTY = new LazyAnnotationBuilder() {
        @Override public LazyAnnotationBuilder addLocalQuickFix(LocalQuickFix quickFix) { return this; }
        @Override public LazyAnnotationBuilder addTextStyles(Collection<TextAttributesKey> keys) { return this; }
        @Override public void create() { }
    };


    LazyAnnotationBuilder addLocalQuickFix(LocalQuickFix quickFix);

    LazyAnnotationBuilder addTextStyles(Collection<TextAttributesKey> keys);

    void create();


    static LazyAnnotationBuilder errorAnnotator(AnnotationHolder holder, PsiElement element, boolean underline, String message) {
        if (element == null || element.getTextRange().isEmpty()) return EMPTY;
        AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
                .highlightType(ProblemHighlightType.ERROR)
                .range(element);

        LazyAnnotationBuilder lazyAnnotator = new B(element, builder);

        if (underline) {
            lazyAnnotator.addTextStyles(Set.of(TscriptSyntaxHighlighter.ERROR_UNDERLINE));
        }
        else {
            lazyAnnotator.addTextStyles(Set.of(TscriptSyntaxHighlighter.ERROR_MARK));
        }

        return lazyAnnotator;
    }

    static LazyAnnotationBuilder warningAnnotation(AnnotationHolder holder, PsiElement element, String message) {
        if (element == null || element.getTextRange().isEmpty()) return EMPTY;
        AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.WARNING, message)
                .highlightType(ProblemHighlightType.WARNING)
                .range(element);

        LazyAnnotationBuilder lazyAnnotator = new B(element, builder);
        lazyAnnotator.addTextStyles(Set.of(TscriptSyntaxHighlighter.WARNING_UNDERLINE));
        return lazyAnnotator;
    }

    static LazyAnnotationBuilder weakWarningAnnotation(AnnotationHolder holder, PsiElement element, String message) {
        if (element == null || element.getTextRange().isEmpty()) return EMPTY;
        AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
                .highlightType(ProblemHighlightType.WEAK_WARNING)
                .range(element);

        LazyAnnotationBuilder lazyAnnotator = new B(element, builder);
        lazyAnnotator.addTextStyles(Set.of(TscriptSyntaxHighlighter.WEAK_WARNING_UNDERLINE));
        return lazyAnnotator;
    }

    static void setTextStyle(AnnotationHolder holder, PsiElement element, Collection<TextAttributesKey> styles) {
        if (element.getTextRange().isEmpty()) return;
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(Styles.mergeAttributes(styles.toArray(new TextAttributesKey[0])))
                .create();
    }

    static void setTextStyle(AnnotationHolder holder, TextRange range, Collection<TextAttributesKey> styles) {
        if (range.isEmpty()) return;
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(range)
                .textAttributes(Styles.mergeAttributes(styles.toArray(new TextAttributesKey[0])))
                .create();
    }


    class B implements LazyAnnotationBuilder {

        private AnnotationBuilder builder;
        private final PsiElement psiElement;
        private final Set<TextAttributesKey> styles = new HashSet<>();

        B(PsiElement psiElement, AnnotationBuilder builder) {
            this.builder = builder;
            this.psiElement = psiElement;
        }

        public LazyAnnotationBuilder addLocalQuickFix(LocalQuickFix quickFix){
            builder = builder.newLocalQuickFix(quickFix, QuickFixDescriptorFactory.create(psiElement)).registerFix();
            return this;
        }

        public LazyAnnotationBuilder addTextStyles(Collection<TextAttributesKey> keys){
            styles.addAll(keys);
            return this;
        }

        public void create(){
            builder.textAttributes(Styles.mergeAttributes(styles.toArray(new TextAttributesKey[0]))).create();
        }

    }


}

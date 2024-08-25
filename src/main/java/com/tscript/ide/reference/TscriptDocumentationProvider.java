package com.tscript.ide.reference;

import com.intellij.codeInsight.documentation.DocumentationManagerUtil;
import com.intellij.icons.AllIcons;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lang.documentation.DocumentationSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil;
import com.intellij.psi.*;
import com.tscript.ide.highlight.TscriptSyntaxHighlighter;
import com.tscript.ide.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Iterator;


public class TscriptDocumentationProvider extends AbstractDocumentationProvider {

    protected static @NotNull StringBuilder appendStyledSpan(
            @NotNull StringBuilder buffer,
            @NotNull TextAttributes attributes,
            @Nullable String value
    ) {
        if (DocumentationSettings.isHighlightingOfQuickDocSignaturesEnabled()) {
            HtmlSyntaxInfoUtil.appendStyledSpan(buffer, attributes, value, DocumentationSettings.getHighlightingSaturation(false));
        }
        else {
            buffer.append(value);
        }
        return buffer;
    }

    private static @NotNull TextAttributes resolveAttributes(@NotNull TextAttributesKey attributesKey) {
        return EditorColorsManager.getInstance().getGlobalScheme().getAttributes(attributesKey);
    }


    @Override
    public @Nullable PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return TscriptASTUtils.resolve(context.getContainingFile(), link);
    }

    @Override
    public @Nullable @Nls String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        String doc = null;

        if (element instanceof TestSingleVar v) {
            doc = getVarDefDoc(v, "Reassigned local variable");
        }

        else if (element instanceof TestSingleConst v) {
            doc = getVarDefDoc(v, "Assign constant");
        }

        else if (element instanceof TestClassDef classDef) {
            doc = getClassDoc(classDef);
        }

        else if (element instanceof TestFunctionDef def){
            doc = getFuncDoc(def);
        }

        else if (element instanceof TestIdentifier ident && ident.getName() != null){
            PsiElement nav = new TscriptDirectNavigationProvider().getNavigationElement(element);
            if (nav != null) {
                doc = generateDoc(nav.getParent(), null);
            }
        }

        else if (element instanceof TestThisExpr){
            TestClassDef nav = TscriptASTUtils.getCurrentClass(element);
            if (nav != null)
                return generateDoc(nav.getParent(), null);
            // else throw new UnsupportedOperationException("can not generate doc for lambda");
        }

        return doc;
    }

    private String getVarDefDoc(PsiNamedElement v, String text){
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.CONTENT_START);
        sb.append(text);
        sb.append(DocumentationMarkup.CONTENT_END);
        sb.append(DocumentationMarkup.DEFINITION_START);
        searchAndAppendVisibility(sb, v);
        appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "var ");
        sb.append(v.getName()).append(" = ");
        try {
            PsiElement element = (PsiElement) v.getClass().getMethod("getExpr").invoke(v);
            if (element == null){
                appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "null");
            }
            else {
                element.accept(new StyledExpressionJoiner(sb));
            }
        }catch (Exception ignored){}

        sb.append(DocumentationMarkup.DEFINITION_END);
        searchAndAppendDocComment(sb, v.getParent().getParent());

        return sb.toString();
    }

    private String getClassDoc(TestClassDef classDef) {
        if (classDef.getName() == null) return null;

        StringBuilder sb = new StringBuilder();

        sb.append(DocumentationMarkup.DEFINITION_START);

        searchAndAppendVisibility(sb, classDef);

        if (classDef.getStaticElement() != null)
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "static ");

        if (classDef.getAbstractElement() != null)
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "abstract ");

        appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "class ");
        appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.CLASS_DEF_NAME), classDef.getName());


        if (classDef.getChainableIdentifier() != null){
            sb.append(": ");
            Iterator<TestIdentifier> itr = classDef.getChainableIdentifier().getIdentifierList().iterator();
            if (itr.hasNext()){
                TestIdentifier accessed = itr.next();
                if (accessed == null) return null;
                sb.append(accessed.getName());

                while (itr.hasNext()){
                    accessed = itr.next();
                    if (accessed == null) return null;
                    sb.append(".").append(accessed.getName());
                }
            }
        }

        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        searchAndAppendDocComment(sb, classDef.getParent());
        sb.append(DocumentationMarkup.CONTENT_END);

        return sb.toString();
    }

    private String getFuncDoc(TestFunctionDef functionDef) {
        if (functionDef.getName() == null) return null;

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);

        TestClassDef currClass = TscriptASTUtils.getCurrentClass(functionDef);
        if (currClass != null){
            sb.append("<icon src='AllIcons.Nodes.Class'/> ");
            DocumentationManagerUtil.createHyperlink(sb, currClass, TscriptASTUtils.fullQualifiedName(currClass), currClass.getName(), true);
            sb.append("<br><br>");
        }

        searchAndAppendVisibility(sb, functionDef);

        if (functionDef.getStaticElement() != null)
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "static ");
        else if (functionDef.getOverriddenElement() != null)
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "overridden ");

        boolean hasArgs = true;
        if (functionDef.getAbstractElement() != null) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "abstract ");
            hasArgs = false;
        }
        else if (functionDef.getNativeElement() != null) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "native ");
            hasArgs = false;
        }

        appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "function ");
        appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.FUNC_DEF_NAME), functionDef.getName());

        if (hasArgs) {
            sb.append("(");
            Iterator<TestParam> itr = functionDef.getParamList().iterator();
            if (itr.hasNext()) {
                sb.append("\n\t");
                appendParamString(sb, itr.next());

                while (itr.hasNext()) {
                    appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.COMMA), ",");
                    sb.append("\n\t");
                    appendParamString(sb, itr.next());
                }
                sb.append("\n)");
            } else {
                sb.append(")");
            }
        }

        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        searchAndAppendDocComment(sb, functionDef.getParent());
        sb.append(DocumentationMarkup.CONTENT_END);

        TestFunctionDef overriddenFunction = TscriptASTUtils.getOverriddenFunction(functionDef);
        if (overriddenFunction != null) {

            sb.append(DocumentationMarkup.SECTIONS_START);
            sb.append(DocumentationMarkup.SECTION_HEADER_START);
            sb.append("overrides");
            sb.append(DocumentationMarkup.SECTION_SEPARATOR);
            sb.append(DocumentationMarkup.SECTION_START);
            DocumentationManagerUtil.createHyperlink(sb, overriddenFunction, TscriptASTUtils.fullQualifiedName(overriddenFunction), overriddenFunction.getName(), true);
            sb.append(" in ");
            TestClassDef superClass = TscriptASTUtils.getCurrentClass(overriddenFunction);
            assert superClass != null;
            DocumentationManagerUtil.createHyperlink(sb, superClass, TscriptASTUtils.fullQualifiedName(superClass), superClass.getName(), true);
            sb.append(DocumentationMarkup.SECTION_END);

            sb.append("<br>");
            sb.append(DocumentationMarkup.GRAYED_START);
            searchAndAppendDocComment(sb, overriddenFunction.getParent());
            sb.append(DocumentationMarkup.GRAYED_END);

            sb.append(DocumentationMarkup.SECTIONS_END);

        }

        return sb.toString();
    }

    private void appendParamString(StringBuilder sb, TestParam param) {
        if (param.getName() == null) return;
        if (param.isConstant())
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "const ");
        sb.append(param.getName());
        if (param.getExpr() != null) {
            sb.append("=");
            param.getExpr().accept(new StyledExpressionJoiner(sb));
        }
    }

    private void searchAndAppendDocComment(StringBuilder builder, PsiElement element){
        while (element != null && !(element instanceof PsiComment)){
            element = element.getPrevSibling();
        }

        if (element == null) return;
        String text = element.getText();
        boolean isBlockComment = false;
        if (text.startsWith("#*")){
            text = text.substring(2);
            isBlockComment = true;
        }
        else text = text.substring(1);

        if (isBlockComment && text.endsWith("*#")) text = text.substring(0, text.length()-2);

        text = text.replaceAll("\n", "<br>");

        builder.append(text);
    }

    private void searchAndAppendVisibility(StringBuilder builder, PsiElement element){
        TestVisibility visibility = TscriptASTUtils.resolveVisibility(element);
        if (visibility != null) {
            appendStyledSpan(builder, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), visibility.getName());
            builder.append(":<br>");
        }
    }


    private static class StyledExpressionJoiner extends TestVisitor {
        private final StringBuilder sb;

        private StyledExpressionJoiner(StringBuilder sb) {
            this.sb = sb;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            element.acceptChildren(this);
        }

        @Override
        public void visitWhiteSpace(@NotNull PsiWhiteSpace space) {
            sb.append(" ");
        }

        @Override
        public void visitNullExpr(@NotNull TestNullExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "null");
        }

        @Override
        public void visitIntegerExpr(@NotNull TestIntegerExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.NUMBER), o.getText());
        }

        @Override
        public void visitBoolExpr(@NotNull TestBoolExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), o.getText());
        }

        @Override
        public void visitStringExpr(@NotNull TestStringExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.STRING), o.getText());
        }

        @Override
        public void visitContainerAccess(@NotNull TestContainerAccess o) {
            sb.append("[");
            if (o.getExpr() != null)
                o.getExpr().accept(this);
            sb.append("]");
        }

        @Override
        public void visitCall(@NotNull TestCall o) {
            sb.append("(");
            if (o.getArgList() == null){
                sb.append(")");
                return;
            }
            Iterator<TestArg> itr = o.getArgList().getArgList().iterator();
            if (itr.hasNext()){
                itr.next().accept(this);
                while (itr.hasNext()){
                    sb.append(", ");
                    itr.next().accept(this);
                }
            }

            sb.append(")");
        }

        @Override
        public void visitArg(@NotNull TestArg o) {
            o.getExpr().accept(this);
        }

        @Override
        public void visitOperation(MixinElements.@NotNull Operation o) {
            sb.append(o.getText());
        }

        @Override
        public void visitMemAccess(@NotNull TestMemAccess o) {
            sb.append(o.getText());
        }

        @Override
        public void visitIdentifier(@NotNull TestIdentifier o) {
            sb.append(o.getName());
        }

        @Override
        public void visitArrayExpr(@NotNull TestArrayExpr o) {
            sb.append("[");
            Iterator<TestExpr> itr = o.getExprList().iterator();
            if (itr.hasNext()){
                itr.next().accept(this);
                while (itr.hasNext()){
                    appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.COMMA), ", ");
                    itr.next().accept(this);
                }
            }
            sb.append("]");
        }

        @Override
        public void visitRealExpr(@NotNull TestRealExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.NUMBER), o.getText());
        }

        @Override
        public void visitRangeExpr(@NotNull TestRangeExpr o) {
            if (o.getExprList().size() != 2) return;
            o.getExprList().get(0).accept(this);
            sb.append(":");
            o.getExprList().get(1).accept(this);
        }

        @Override
        public void visitSuperAccess(@NotNull TestSuperAccess o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "super");
            sb.append(".");
            sb.append(o.getName());
        }

        @Override
        public void visitThisExpr(@NotNull TestThisExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "this");
        }

        @Override
        public void visitTypeofPrefixExpr(@NotNull TestTypeofPrefixExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "typeof ");
            o.getExpr().accept(this);
        }

        @Override
        public void visitNotExpr(@NotNull TestNotExpr o) {
            appendStyledSpan(sb, resolveAttributes(TscriptSyntaxHighlighter.KEYWORD), "not ");
            o.getExpr().accept(this);
        }

        @Override
        public void visitNegationExpr(@NotNull TestNegationExpr o) {
            sb.append("-");
            o.getExpr().accept(this);
        }

    }

}

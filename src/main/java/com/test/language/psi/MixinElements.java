package com.test.language.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface MixinElements {

    /* FUNCTIONS */

    interface TestFunctionDef extends PsiNameIdentifierOwner { }

    class TestFunctionDefMixin extends ASTWrapperPsiElement implements TestFunctionDef {

        public TestFunctionDefMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public PsiElement setName(@NotNull String newName) {
            // Implementation for renaming the element
            return this;
        }

        @Nullable
        @Override
        public PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT); // Assuming IDENT is the type of the identifier token
        }

        public PsiElement getStaticElement(){
            return findChildByType(TestTypes.STATIC);
        }

        public PsiElement getAbstractElement(){
            return findChildByType(TestTypes.ABSTRACT);
        }

        public PsiElement getNativeElement(){
            return findChildByType(TestTypes.NATIVE);
        }

        public PsiElement getOverriddenElement(){
            return findChildByType(TestTypes.STATIC);
        }
    }


    interface TestClassDef extends PsiNameIdentifierOwner { }

    /* CLASSES */

    class TestClassDefMixin extends ASTWrapperPsiElement implements TestClassDef {

        public TestClassDefMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public PsiElement setName(@NotNull String newName) {
            // Implementation for renaming the element
            return this;
        }

        public TestChainableIdentifier getSuper(){
            return findChildByClass(TestChainableIdentifier.class);
        }


        @Nullable
        @Override
        public PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT); // Assuming IDENT is the type of the identifier token
        }

        public PsiElement getStaticElement(){
            return findChildByType(TestTypes.STATIC);
        }

        public PsiElement getAbstractElement(){
            return findChildByType(TestTypes.ABSTRACT);
        }
    }


    interface TestNSpaceDef extends PsiNameIdentifierOwner { }

    class TestNSpaceDefMixin extends ASTWrapperPsiElement implements TestNSpaceDef {

        public TestNSpaceDefMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public PsiElement setName(@NotNull String newName) {
            // Implementation for renaming the element
            return this;
        }

        @Nullable
        @Override
        public PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT); // Assuming IDENT is the type of the identifier token
        }

        public PsiElement getStaticElement(){
            return findChildByType(TestTypes.STATIC);
        }
    }

    interface StaticAccessor extends PsiElement {
        PsiElement getStaticElement();
    }

    class VarDefListMixin extends ASTWrapperPsiElement implements StaticAccessor {

        public VarDefListMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public PsiElement getStaticElement() {
            return findChildByType(TestTypes.STATIC);
        }
    }

    interface VariableDef extends PsiNameIdentifierOwner {}

    class VariableDefMixin extends ASTWrapperPsiElement implements VariableDef {

        public VariableDefMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }
    }

    /* IDENTIFIER */
    interface IdentifierDef extends PsiNameIdentifierOwner {}

    class IdentifierMixin extends ASTWrapperPsiElement implements IdentifierDef {

        public IdentifierMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }
    }


    interface ParameterDef extends PsiNameIdentifierOwner {}

    class ParameterMixin extends ASTWrapperPsiElement implements ParameterDef {

        public ParameterMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }

        public boolean isConstant(){
            return findChildByType(TestTypes.CONST) != null;
        }
    }


    interface SuperMemAccess extends PsiNameIdentifierOwner {}

    class SuperMemAccessMixin extends ASTWrapperPsiElement implements SuperMemAccess {

        public SuperMemAccessMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }

        public boolean isConstant(){
            return findChildByType(TestTypes.CONST) != null;
        }
    }


    interface Visibility extends PsiNameIdentifierOwner {}

    class VisibilityMixin extends ASTWrapperPsiElement implements SuperMemAccess {

        public VisibilityMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            PsiElement v = findChildByType(TestTypes.PUBLIC);
            if (v != null) return v;
            v = findChildByType(TestTypes.PRIVATE);
            if (v != null) return v;
            return findChildByType(TestTypes.PROTECTED);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }
    }


    interface Operation extends PsiElement {}

    class OperationMixin extends ASTWrapperPsiElement implements Operation {

        public OperationMixin(@NotNull ASTNode node) {
            super(node);
        }

        public <T extends PsiElement> T findChildByType(IElementType type){
            return super.findChildByType(type);
        }

    }



    interface TryCatch extends PsiNameIdentifierOwner {}

    class TryCatchMixin extends ASTWrapperPsiElement implements TryCatch {

        public TryCatchMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }
    }



    interface ForLoop extends PsiNameIdentifierOwner {}

    class ForLoopMixin extends ASTWrapperPsiElement implements ForLoop {

        public ForLoopMixin(@NotNull ASTNode node) {
            super(node);
        }

        @Override
        public String getName() {
            PsiElement identifier = getNameIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        @Override
        public @Nullable PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT);
        }

        @Override
        public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
            return this;
        }
    }

}

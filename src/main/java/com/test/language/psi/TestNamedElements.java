package com.test.language.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestNamedElements {

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

        public String getSuperName(){
            PsiElement identifier = getSuperClassIdentifier();
            return identifier != null ? identifier.getText() : null;
        }

        public PsiElement getSuperClassIdentifier(){
            PsiElement colon = findChildByType(TestTypes.COLON);
            if (colon != null) {
                PsiElement nextSibling = colon.getNextSibling();
                while (nextSibling != null && !(nextSibling.getNode().getElementType() == TestTypes.IDENT)) {
                    nextSibling = nextSibling.getNextSibling();
                }
                return nextSibling;
            }
            return null;
        }

        @Nullable
        @Override
        public PsiElement getNameIdentifier() {
            return findChildByType(TestTypes.IDENT); // Assuming IDENT is the type of the identifier token
        }
    }

}

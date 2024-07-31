// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.test.language.psi.impl.*;

public interface TestTypes {

  IElementType BLOCK = new TestElementType("BLOCK");
  IElementType FUNCTION_DEF = new TestElementType("FUNCTION_DEF");
  IElementType MODIFIERS = new TestElementType("MODIFIERS");
  IElementType NAMESPACE_DEF = new TestElementType("NAMESPACE_DEF");
  IElementType PARAM = new TestElementType("PARAM");
  IElementType PARAMS = new TestElementType("PARAMS");
  IElementType STMT_LIST = new TestElementType("STMT_LIST");
  IElementType VAR_DEC = new TestElementType("VAR_DEC");

  IElementType ASSIGN = new TestTokenType("ASSIGN");
  IElementType COMMA = new TestTokenType("COMMA");
  IElementType COMMENT = new TestTokenType("COMMENT");
  IElementType CONST = new TestTokenType("CONST");
  IElementType CURLY_CLOSE = new TestTokenType("CURLY_CLOSE");
  IElementType CURLY_OPEN = new TestTokenType("CURLY_OPEN");
  IElementType FUNCTION = new TestTokenType("FUNCTION");
  IElementType IDENT = new TestTokenType("IDENT");
  IElementType NAMESPACE = new TestTokenType("NAMESPACE");
  IElementType NATIVE = new TestTokenType("NATIVE");
  IElementType NUM = new TestTokenType("NUM");
  IElementType OVERRIDDEN = new TestTokenType("OVERRIDDEN");
  IElementType PAREN_CLOSE = new TestTokenType("PAREN_CLOSE");
  IElementType PAREN_OPEN = new TestTokenType("PAREN_OPEN");
  IElementType PRIVATE = new TestTokenType("PRIVATE");
  IElementType PROTECTED = new TestTokenType("PROTECTED");
  IElementType PUBLIC = new TestTokenType("PUBLIC");
  IElementType SEMI = new TestTokenType("SEMI");
  IElementType VAR = new TestTokenType("VAR");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLOCK) {
        return new TestBlockImpl(node);
      }
      else if (type == FUNCTION_DEF) {
        return new TestFunctionDefImpl(node);
      }
      else if (type == MODIFIERS) {
        return new TestModifiersImpl(node);
      }
      else if (type == NAMESPACE_DEF) {
        return new TestNamespaceDefImpl(node);
      }
      else if (type == PARAM) {
        return new TestParamImpl(node);
      }
      else if (type == PARAMS) {
        return new TestParamsImpl(node);
      }
      else if (type == STMT_LIST) {
        return new TestStmtListImpl(node);
      }
      else if (type == VAR_DEC) {
        return new TestVarDecImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.test.language.psi.impl.*;

public interface TestTypes {

  IElementType ARG = new TestElementType("ARG");
  IElementType ARRAY = new TestElementType("ARRAY");
  IElementType BLOCK = new TestElementType("BLOCK");
  IElementType CALL = new TestElementType("CALL");
  IElementType CLASS_DEF = new TestElementType("CLASS_DEF");
  IElementType CLOSURE = new TestElementType("CLOSURE");
  IElementType CLOSURES = new TestElementType("CLOSURES");
  IElementType CONSTRUCTOR_DEF = new TestElementType("CONSTRUCTOR_DEF");
  IElementType CONTAINER_ACCESS = new TestElementType("CONTAINER_ACCESS");
  IElementType COST_DEC = new TestElementType("COST_DEC");
  IElementType DICTIONARY = new TestElementType("DICTIONARY");
  IElementType DICTIONARY_CONTENT = new TestElementType("DICTIONARY_CONTENT");
  IElementType DICTIONARY_ENTRY = new TestElementType("DICTIONARY_ENTRY");
  IElementType DO_WHILE = new TestElementType("DO_WHILE");
  IElementType FOR_LOOP = new TestElementType("FOR_LOOP");
  IElementType FUNCTION_DEF = new TestElementType("FUNCTION_DEF");
  IElementType IF_ELSE = new TestElementType("IF_ELSE");
  IElementType LAMBDA = new TestElementType("LAMBDA");
  IElementType MEMBER_ACCESS = new TestElementType("MEMBER_ACCESS");
  IElementType NAMESPACE_DEF = new TestElementType("NAMESPACE_DEF");
  IElementType PARAM = new TestElementType("PARAM");
  IElementType PARAMS = new TestElementType("PARAMS");
  IElementType STMT_LIST = new TestElementType("STMT_LIST");
  IElementType SUPER_ACCESS = new TestElementType("SUPER_ACCESS");
  IElementType THROW_STMT = new TestElementType("THROW_STMT");
  IElementType TRY_CATCH = new TestElementType("TRY_CATCH");
  IElementType TYPEOF_PREFIX_EXPR = new TestElementType("TYPEOF_PREFIX_EXPR");
  IElementType VAR_DEC = new TestElementType("VAR_DEC");
  IElementType VISIBILITY = new TestElementType("VISIBILITY");
  IElementType WHILE_DO = new TestElementType("WHILE_DO");

  IElementType ABSTRACT = new TestTokenType("ABSTRACT");
  IElementType ASSIGN = new TestTokenType("ASSIGN");
  IElementType BRACKET_CLOSE = new TestTokenType("BRACKET_CLOSE");
  IElementType BRACKET_OPEN = new TestTokenType("BRACKET_OPEN");
  IElementType BREAK = new TestTokenType("BREAK");
  IElementType CATCH = new TestTokenType("CATCH");
  IElementType CLASS = new TestTokenType("CLASS");
  IElementType COLON = new TestTokenType("COLON");
  IElementType COMMA = new TestTokenType("COMMA");
  IElementType COMMENT = new TestTokenType("COMMENT");
  IElementType CONST = new TestTokenType("CONST");
  IElementType CONSTRUCTOR = new TestTokenType("CONSTRUCTOR");
  IElementType CONTINUE = new TestTokenType("CONTINUE");
  IElementType CURLY_CLOSE = new TestTokenType("CURLY_CLOSE");
  IElementType CURLY_OPEN = new TestTokenType("CURLY_OPEN");
  IElementType DO = new TestTokenType("DO");
  IElementType DOT = new TestTokenType("DOT");
  IElementType ELSE = new TestTokenType("ELSE");
  IElementType FALSE = new TestTokenType("FALSE");
  IElementType FOR = new TestTokenType("FOR");
  IElementType FUNCTION = new TestTokenType("FUNCTION");
  IElementType IDENT = new TestTokenType("IDENT");
  IElementType IF = new TestTokenType("IF");
  IElementType IN = new TestTokenType("IN");
  IElementType INTEGER = new TestTokenType("INTEGER");
  IElementType NAMESPACE = new TestTokenType("NAMESPACE");
  IElementType NATIVE = new TestTokenType("NATIVE");
  IElementType NULL = new TestTokenType("NULL");
  IElementType OVERRIDDEN = new TestTokenType("OVERRIDDEN");
  IElementType PAREN_CLOSE = new TestTokenType("PAREN_CLOSE");
  IElementType PAREN_OPEN = new TestTokenType("PAREN_OPEN");
  IElementType PRIVATE = new TestTokenType("PRIVATE");
  IElementType PROTECTED = new TestTokenType("PROTECTED");
  IElementType PUBLIC = new TestTokenType("PUBLIC");
  IElementType REAL = new TestTokenType("REAL");
  IElementType SEMI = new TestTokenType("SEMI");
  IElementType STATIC = new TestTokenType("STATIC");
  IElementType STRING = new TestTokenType("STRING");
  IElementType SUPER = new TestTokenType("SUPER");
  IElementType THEN = new TestTokenType("THEN");
  IElementType THIS = new TestTokenType("THIS");
  IElementType THROW = new TestTokenType("THROW");
  IElementType TRUE = new TestTokenType("TRUE");
  IElementType TRY = new TestTokenType("TRY");
  IElementType TYPEOF = new TestTokenType("TYPEOF");
  IElementType VAR = new TestTokenType("VAR");
  IElementType WHILE = new TestTokenType("WHILE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARG) {
        return new TestArgImpl(node);
      }
      else if (type == ARRAY) {
        return new TestArrayImpl(node);
      }
      else if (type == BLOCK) {
        return new TestBlockImpl(node);
      }
      else if (type == CALL) {
        return new TestCallImpl(node);
      }
      else if (type == CLASS_DEF) {
        return new TestClassDefImpl(node);
      }
      else if (type == CLOSURE) {
        return new TestClosureImpl(node);
      }
      else if (type == CLOSURES) {
        return new TestClosuresImpl(node);
      }
      else if (type == CONSTRUCTOR_DEF) {
        return new TestConstructorDefImpl(node);
      }
      else if (type == CONTAINER_ACCESS) {
        return new TestContainerAccessImpl(node);
      }
      else if (type == COST_DEC) {
        return new TestCostDecImpl(node);
      }
      else if (type == DICTIONARY) {
        return new TestDictionaryImpl(node);
      }
      else if (type == DICTIONARY_CONTENT) {
        return new TestDictionaryContentImpl(node);
      }
      else if (type == DICTIONARY_ENTRY) {
        return new TestDictionaryEntryImpl(node);
      }
      else if (type == DO_WHILE) {
        return new TestDoWhileImpl(node);
      }
      else if (type == FOR_LOOP) {
        return new TestForLoopImpl(node);
      }
      else if (type == FUNCTION_DEF) {
        return new TestFunctionDefImpl(node);
      }
      else if (type == IF_ELSE) {
        return new TestIfElseImpl(node);
      }
      else if (type == LAMBDA) {
        return new TestLambdaImpl(node);
      }
      else if (type == MEMBER_ACCESS) {
        return new TestMemberAccessImpl(node);
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
      else if (type == SUPER_ACCESS) {
        return new TestSuperAccessImpl(node);
      }
      else if (type == THROW_STMT) {
        return new TestThrowStmtImpl(node);
      }
      else if (type == TRY_CATCH) {
        return new TestTryCatchImpl(node);
      }
      else if (type == TYPEOF_PREFIX_EXPR) {
        return new TestTypeofPrefixExprImpl(node);
      }
      else if (type == VAR_DEC) {
        return new TestVarDecImpl(node);
      }
      else if (type == VISIBILITY) {
        return new TestVisibilityImpl(node);
      }
      else if (type == WHILE_DO) {
        return new TestWhileDoImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

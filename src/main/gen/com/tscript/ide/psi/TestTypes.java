// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.tscript.ide.psi.impl.*;

public interface TestTypes {

  IElementType AND_EXPR = new TestElementType("AND_EXPR");
  IElementType ARG = new TestElementType("ARG");
  IElementType ARG_LIST = new TestElementType("ARG_LIST");
  IElementType ARRAY_EXPR = new TestElementType("ARRAY_EXPR");
  IElementType ASSIGN_EXPR = new TestElementType("ASSIGN_EXPR");
  IElementType ASSIGN_OP = new TestElementType("ASSIGN_OP");
  IElementType BLOCK = new TestElementType("BLOCK");
  IElementType BOOL_EXPR = new TestElementType("BOOL_EXPR");
  IElementType BREAK_STMT = new TestElementType("BREAK_STMT");
  IElementType CALL = new TestElementType("CALL");
  IElementType CHAINABLE_IDENTIFIER = new TestElementType("CHAINABLE_IDENTIFIER");
  IElementType CLASS_BODY_DEF = new TestElementType("CLASS_BODY_DEF");
  IElementType CLASS_DEF = new TestElementType("CLASS_DEF");
  IElementType CLOSURE = new TestElementType("CLOSURE");
  IElementType COMP_EXPR = new TestElementType("COMP_EXPR");
  IElementType COMP_OP = new TestElementType("COMP_OP");
  IElementType CONSTRUCTOR_DEF = new TestElementType("CONSTRUCTOR_DEF");
  IElementType CONST_DEC = new TestElementType("CONST_DEC");
  IElementType CONTAINER_ACCESS = new TestElementType("CONTAINER_ACCESS");
  IElementType CONTINUE_STMT = new TestElementType("CONTINUE_STMT");
  IElementType DEFINITION = new TestElementType("DEFINITION");
  IElementType DICTIONARY_ENTRY = new TestElementType("DICTIONARY_ENTRY");
  IElementType DICTIONARY_EXPR = new TestElementType("DICTIONARY_EXPR");
  IElementType DO_WHILE = new TestElementType("DO_WHILE");
  IElementType EQ_EXPR = new TestElementType("EQ_EXPR");
  IElementType EQ_OP = new TestElementType("EQ_OP");
  IElementType EXPR = new TestElementType("EXPR");
  IElementType FOR_LOOP = new TestElementType("FOR_LOOP");
  IElementType FROM_IMPORT = new TestElementType("FROM_IMPORT");
  IElementType FROM_USE = new TestElementType("FROM_USE");
  IElementType FUNCTION_DEF = new TestElementType("FUNCTION_DEF");
  IElementType IDENTIFIER = new TestElementType("IDENTIFIER");
  IElementType IF_ELSE = new TestElementType("IF_ELSE");
  IElementType IMPORT_STMT = new TestElementType("IMPORT_STMT");
  IElementType INTEGER_EXPR = new TestElementType("INTEGER_EXPR");
  IElementType LAMBDA_EXPR = new TestElementType("LAMBDA_EXPR");
  IElementType MEM_ACCESS = new TestElementType("MEM_ACCESS");
  IElementType MUL_EXPR = new TestElementType("MUL_EXPR");
  IElementType MUL_OP = new TestElementType("MUL_OP");
  IElementType NAMESPACE_DEF = new TestElementType("NAMESPACE_DEF");
  IElementType NEGATION_EXPR = new TestElementType("NEGATION_EXPR");
  IElementType NOT_EXPR = new TestElementType("NOT_EXPR");
  IElementType NULL_EXPR = new TestElementType("NULL_EXPR");
  IElementType OR_EXPR = new TestElementType("OR_EXPR");
  IElementType PARAM = new TestElementType("PARAM");
  IElementType PLUS_EXPR = new TestElementType("PLUS_EXPR");
  IElementType PLUS_OP = new TestElementType("PLUS_OP");
  IElementType POSIVATION_EXPR = new TestElementType("POSIVATION_EXPR");
  IElementType POW_EXPR = new TestElementType("POW_EXPR");
  IElementType RANGE_EXPR = new TestElementType("RANGE_EXPR");
  IElementType REAL_EXPR = new TestElementType("REAL_EXPR");
  IElementType RETURN_STMT = new TestElementType("RETURN_STMT");
  IElementType SHIFT_EXPR = new TestElementType("SHIFT_EXPR");
  IElementType SHIFT_OP = new TestElementType("SHIFT_OP");
  IElementType SINGLE_CONST = new TestElementType("SINGLE_CONST");
  IElementType SINGLE_VAR = new TestElementType("SINGLE_VAR");
  IElementType STMT = new TestElementType("STMT");
  IElementType STRING_EXPR = new TestElementType("STRING_EXPR");
  IElementType SUPER_ACCESS = new TestElementType("SUPER_ACCESS");
  IElementType THIS_EXPR = new TestElementType("THIS_EXPR");
  IElementType THROW_STMT = new TestElementType("THROW_STMT");
  IElementType TRY_CATCH = new TestElementType("TRY_CATCH");
  IElementType TYPEOF_PREFIX_EXPR = new TestElementType("TYPEOF_PREFIX_EXPR");
  IElementType UNARY_EXPR = new TestElementType("UNARY_EXPR");
  IElementType USE_STMT = new TestElementType("USE_STMT");
  IElementType VAR_DEC = new TestElementType("VAR_DEC");
  IElementType VISIBILITY = new TestElementType("VISIBILITY");
  IElementType WHILE_DO = new TestElementType("WHILE_DO");
  IElementType XOR_EXPR = new TestElementType("XOR_EXPR");

  IElementType ABSTRACT = new TestTokenType("ABSTRACT");
  IElementType ADD = new TestTokenType("ADD");
  IElementType ADD_ASSIGN = new TestTokenType("ADD_ASSIGN");
  IElementType AND = new TestTokenType("AND");
  IElementType ASSIGN = new TestTokenType("ASSIGN");
  IElementType BLOCK_COMMENT = new TestTokenType("BLOCK_COMMENT");
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
  IElementType DIV = new TestTokenType("DIV");
  IElementType DIV_ASSIGN = new TestTokenType("DIV_ASSIGN");
  IElementType DO = new TestTokenType("DO");
  IElementType DOT = new TestTokenType("DOT");
  IElementType ELSE = new TestTokenType("ELSE");
  IElementType EQUALS = new TestTokenType("EQUALS");
  IElementType FALSE = new TestTokenType("FALSE");
  IElementType FOR = new TestTokenType("FOR");
  IElementType FROM = new TestTokenType("FROM");
  IElementType FUNCTION = new TestTokenType("FUNCTION");
  IElementType GEQ = new TestTokenType("GEQ");
  IElementType GT = new TestTokenType("GT");
  IElementType IDENT = new TestTokenType("IDENT");
  IElementType IDIV = new TestTokenType("IDIV");
  IElementType IDIV_ASSIGN = new TestTokenType("IDIV_ASSIGN");
  IElementType IF = new TestTokenType("IF");
  IElementType IMPORT = new TestTokenType("IMPORT");
  IElementType IN = new TestTokenType("IN");
  IElementType INTEGER = new TestTokenType("INTEGER");
  IElementType LEQ = new TestTokenType("LEQ");
  IElementType LT = new TestTokenType("LT");
  IElementType MOD = new TestTokenType("MOD");
  IElementType MOD_ASSIGN = new TestTokenType("MOD_ASSIGN");
  IElementType MUL = new TestTokenType("MUL");
  IElementType MUL_ASSIGN = new TestTokenType("MUL_ASSIGN");
  IElementType NAMESPACE = new TestTokenType("NAMESPACE");
  IElementType NATIVE = new TestTokenType("NATIVE");
  IElementType NOT = new TestTokenType("NOT");
  IElementType NOT_EQUALS = new TestTokenType("NOT_EQUALS");
  IElementType NULL = new TestTokenType("NULL");
  IElementType OR = new TestTokenType("OR");
  IElementType OVERRIDDEN = new TestTokenType("OVERRIDDEN");
  IElementType PAREN_CLOSE = new TestTokenType("PAREN_CLOSE");
  IElementType PAREN_OPEN = new TestTokenType("PAREN_OPEN");
  IElementType POW = new TestTokenType("POW");
  IElementType POW_ASSIGN = new TestTokenType("POW_ASSIGN");
  IElementType PRIVATE = new TestTokenType("PRIVATE");
  IElementType PROTECTED = new TestTokenType("PROTECTED");
  IElementType PUBLIC = new TestTokenType("PUBLIC");
  IElementType REAL = new TestTokenType("REAL");
  IElementType RETURN = new TestTokenType("RETURN");
  IElementType SAL = new TestTokenType("SAL");
  IElementType SAL_ASSIGN = new TestTokenType("SAL_ASSIGN");
  IElementType SAR = new TestTokenType("SAR");
  IElementType SAR_ASSIGN = new TestTokenType("SAR_ASSIGN");
  IElementType SEMI = new TestTokenType("SEMI");
  IElementType SLR = new TestTokenType("SLR");
  IElementType SLR_ASSIGN = new TestTokenType("SLR_ASSIGN");
  IElementType STATIC = new TestTokenType("STATIC");
  IElementType STRING = new TestTokenType("STRING");
  IElementType SUB = new TestTokenType("SUB");
  IElementType SUB_ASSIGN = new TestTokenType("SUB_ASSIGN");
  IElementType SUPER = new TestTokenType("SUPER");
  IElementType THEN = new TestTokenType("THEN");
  IElementType THIS = new TestTokenType("THIS");
  IElementType THROW = new TestTokenType("THROW");
  IElementType TRUE = new TestTokenType("TRUE");
  IElementType TRY = new TestTokenType("TRY");
  IElementType TYPEOF = new TestTokenType("TYPEOF");
  IElementType USE = new TestTokenType("USE");
  IElementType VAR = new TestTokenType("VAR");
  IElementType WHILE = new TestTokenType("WHILE");
  IElementType XOR = new TestTokenType("XOR");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == AND_EXPR) {
        return new TestAndExprImpl(node);
      }
      else if (type == ARG) {
        return new TestArgImpl(node);
      }
      else if (type == ARG_LIST) {
        return new TestArgListImpl(node);
      }
      else if (type == ARRAY_EXPR) {
        return new TestArrayExprImpl(node);
      }
      else if (type == ASSIGN_EXPR) {
        return new TestAssignExprImpl(node);
      }
      else if (type == ASSIGN_OP) {
        return new TestAssignOpImpl(node);
      }
      else if (type == BLOCK) {
        return new TestBlockImpl(node);
      }
      else if (type == BOOL_EXPR) {
        return new TestBoolExprImpl(node);
      }
      else if (type == BREAK_STMT) {
        return new TestBreakStmtImpl(node);
      }
      else if (type == CALL) {
        return new TestCallImpl(node);
      }
      else if (type == CHAINABLE_IDENTIFIER) {
        return new TestChainableIdentifierImpl(node);
      }
      else if (type == CLASS_BODY_DEF) {
        return new TestClassBodyDefImpl(node);
      }
      else if (type == CLASS_DEF) {
        return new TestClassDefImpl(node);
      }
      else if (type == CLOSURE) {
        return new TestClosureImpl(node);
      }
      else if (type == COMP_EXPR) {
        return new TestCompExprImpl(node);
      }
      else if (type == COMP_OP) {
        return new TestCompOpImpl(node);
      }
      else if (type == CONSTRUCTOR_DEF) {
        return new TestConstructorDefImpl(node);
      }
      else if (type == CONST_DEC) {
        return new TestConstDecImpl(node);
      }
      else if (type == CONTAINER_ACCESS) {
        return new TestContainerAccessImpl(node);
      }
      else if (type == CONTINUE_STMT) {
        return new TestContinueStmtImpl(node);
      }
      else if (type == DEFINITION) {
        return new TestDefinitionImpl(node);
      }
      else if (type == DICTIONARY_ENTRY) {
        return new TestDictionaryEntryImpl(node);
      }
      else if (type == DICTIONARY_EXPR) {
        return new TestDictionaryExprImpl(node);
      }
      else if (type == DO_WHILE) {
        return new TestDoWhileImpl(node);
      }
      else if (type == EQ_EXPR) {
        return new TestEqExprImpl(node);
      }
      else if (type == EQ_OP) {
        return new TestEqOpImpl(node);
      }
      else if (type == FOR_LOOP) {
        return new TestForLoopImpl(node);
      }
      else if (type == FROM_IMPORT) {
        return new TestFromImportImpl(node);
      }
      else if (type == FROM_USE) {
        return new TestFromUseImpl(node);
      }
      else if (type == FUNCTION_DEF) {
        return new TestFunctionDefImpl(node);
      }
      else if (type == IDENTIFIER) {
        return new TestIdentifierImpl(node);
      }
      else if (type == IF_ELSE) {
        return new TestIfElseImpl(node);
      }
      else if (type == IMPORT_STMT) {
        return new TestImportStmtImpl(node);
      }
      else if (type == INTEGER_EXPR) {
        return new TestIntegerExprImpl(node);
      }
      else if (type == LAMBDA_EXPR) {
        return new TestLambdaExprImpl(node);
      }
      else if (type == MEM_ACCESS) {
        return new TestMemAccessImpl(node);
      }
      else if (type == MUL_EXPR) {
        return new TestMulExprImpl(node);
      }
      else if (type == MUL_OP) {
        return new TestMulOpImpl(node);
      }
      else if (type == NAMESPACE_DEF) {
        return new TestNamespaceDefImpl(node);
      }
      else if (type == NEGATION_EXPR) {
        return new TestNegationExprImpl(node);
      }
      else if (type == NOT_EXPR) {
        return new TestNotExprImpl(node);
      }
      else if (type == NULL_EXPR) {
        return new TestNullExprImpl(node);
      }
      else if (type == OR_EXPR) {
        return new TestOrExprImpl(node);
      }
      else if (type == PARAM) {
        return new TestParamImpl(node);
      }
      else if (type == PLUS_EXPR) {
        return new TestPlusExprImpl(node);
      }
      else if (type == PLUS_OP) {
        return new TestPlusOpImpl(node);
      }
      else if (type == POSIVATION_EXPR) {
        return new TestPosivationExprImpl(node);
      }
      else if (type == POW_EXPR) {
        return new TestPowExprImpl(node);
      }
      else if (type == RANGE_EXPR) {
        return new TestRangeExprImpl(node);
      }
      else if (type == REAL_EXPR) {
        return new TestRealExprImpl(node);
      }
      else if (type == RETURN_STMT) {
        return new TestReturnStmtImpl(node);
      }
      else if (type == SHIFT_EXPR) {
        return new TestShiftExprImpl(node);
      }
      else if (type == SHIFT_OP) {
        return new TestShiftOpImpl(node);
      }
      else if (type == SINGLE_CONST) {
        return new TestSingleConstImpl(node);
      }
      else if (type == SINGLE_VAR) {
        return new TestSingleVarImpl(node);
      }
      else if (type == STMT) {
        return new TestStmtImpl(node);
      }
      else if (type == STRING_EXPR) {
        return new TestStringExprImpl(node);
      }
      else if (type == SUPER_ACCESS) {
        return new TestSuperAccessImpl(node);
      }
      else if (type == THIS_EXPR) {
        return new TestThisExprImpl(node);
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
      else if (type == UNARY_EXPR) {
        return new TestUnaryExprImpl(node);
      }
      else if (type == USE_STMT) {
        return new TestUseStmtImpl(node);
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
      else if (type == XOR_EXPR) {
        return new TestXorExprImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

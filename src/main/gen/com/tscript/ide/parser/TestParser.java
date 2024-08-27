// This is a generated file. Not intended for manual editing.
package com.tscript.ide.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.tscript.ide.psi.TestTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TestParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(AND_EXPR, ARRAY_EXPR, ASSIGN_EXPR, BOOL_EXPR,
      COMP_EXPR, DICTIONARY_EXPR, EQ_EXPR, EXPR,
      INTEGER_EXPR, LAMBDA_EXPR, MUL_EXPR, NEGATION_EXPR,
      NOT_EXPR, NULL_EXPR, OR_EXPR, PLUS_EXPR,
      POSIVATION_EXPR, POW_EXPR, RANGE_EXPR, REAL_EXPR,
      SHIFT_EXPR, STRING_EXPR, THIS_EXPR, TYPEOF_PREFIX_EXPR,
      UNARY_EXPR, XOR_EXPR),
  };

  /* ********************************************************** */
  // or_expr (AND and_expr)*
  public static boolean and_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, AND_EXPR, "<and expr>");
    r = or_expr(b, l + 1);
    r = r && and_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (AND and_expr)*
  private static boolean and_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!and_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "and_expr_1", c)) break;
    }
    return true;
  }

  // AND and_expr
  private static boolean and_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && and_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FUNCTION function_def
  static boolean anon_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anon_function")) return false;
    if (!nextTokenIs(b, FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, FUNCTION);
    p = r; // pin = 1
    r = r && function_def(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (IDENT ASSIGN)? expr
  public static boolean arg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARG, "<arg>");
    r = arg_0(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (IDENT ASSIGN)?
  private static boolean arg_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_0")) return false;
    arg_0_0(b, l + 1);
    return true;
  }

  // IDENT ASSIGN
  private static boolean arg_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENT, ASSIGN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (arg (COMMA arg)*)?
  public static boolean arg_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list")) return false;
    Marker m = enter_section_(b, l, _NONE_, ARG_LIST, "<arg list>");
    arg_list_0(b, l + 1);
    exit_section_(b, l, m, true, false, TestParser::recover_params);
    return true;
  }

  // arg (COMMA arg)*
  private static boolean arg_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = arg(b, l + 1);
    r = r && arg_list_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA arg)*
  private static boolean arg_list_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arg_list_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arg_list_0_1", c)) break;
    }
    return true;
  }

  // COMMA arg
  private static boolean arg_list_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && arg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr (COMMA expr)*
  static boolean array_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    r = r && array_content_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_array);
    return r;
  }

  // (COMMA expr)*
  private static boolean array_content_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_content_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_content_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_content_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean array_content_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_content_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BRACKET_OPEN BRACKET_CLOSE | BRACKET_OPEN array_content BRACKET_CLOSE
  public static boolean array_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expr")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, BRACKET_OPEN, BRACKET_CLOSE);
    if (!r) r = array_expr_1(b, l + 1);
    exit_section_(b, m, ARRAY_EXPR, r);
    return r;
  }

  // BRACKET_OPEN array_content BRACKET_CLOSE
  private static boolean array_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && array_content(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // and_expr assign_op assign_expr | and_expr
  public static boolean assign_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, ASSIGN_EXPR, "<assign expr>");
    r = assign_expr_0(b, l + 1);
    if (!r) r = and_expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // and_expr assign_op assign_expr
  private static boolean assign_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = and_expr(b, l + 1);
    r = r && assign_op(b, l + 1);
    r = r && assign_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ASSIGN | ADD_ASSIGN | SUB_ASSIGN | MUL_ASSIGN
  // | DIV_ASSIGN | IDIV_ASSIGN | MOD_ASSIGN | POW_ASSIGN | SAL_ASSIGN | SAR_ASSIGN | SLR_ASSIGN
  public static boolean assign_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_OP, "<assign op>");
    r = consumeToken(b, ASSIGN);
    if (!r) r = consumeToken(b, ADD_ASSIGN);
    if (!r) r = consumeToken(b, SUB_ASSIGN);
    if (!r) r = consumeToken(b, MUL_ASSIGN);
    if (!r) r = consumeToken(b, DIV_ASSIGN);
    if (!r) r = consumeToken(b, IDIV_ASSIGN);
    if (!r) r = consumeToken(b, MOD_ASSIGN);
    if (!r) r = consumeToken(b, POW_ASSIGN);
    if (!r) r = consumeToken(b, SAL_ASSIGN);
    if (!r) r = consumeToken(b, SAR_ASSIGN);
    if (!r) r = consumeToken(b, SLR_ASSIGN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CURLY_OPEN stmt_list CURLY_CLOSE
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK, null);
    r = consumeToken(b, CURLY_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, stmt_list(b, l + 1));
    r = p && consumeToken(b, CURLY_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // TRUE | FALSE
  public static boolean bool_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bool_expr")) return false;
    if (!nextTokenIs(b, "<bool expr>", FALSE, TRUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOL_EXPR, "<bool expr>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BREAK SEMI
  public static boolean break_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_stmt")) return false;
    if (!nextTokenIs(b, BREAK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BREAK_STMT, null);
    r = consumeTokens(b, 1, BREAK, SEMI);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PAREN_OPEN arg_list PAREN_CLOSE
  public static boolean call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CALL, null);
    r = consumeToken(b, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, arg_list(b, l + 1));
    r = p && consumeToken(b, PAREN_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier (DOT identifier)*
  public static boolean chainable_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chainable_identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CHAINABLE_IDENTIFIER, "<chainable identifier>");
    r = identifier(b, l + 1);
    r = r && chainable_identifier_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_global);
    return r;
  }

  // (DOT identifier)*
  private static boolean chainable_identifier_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chainable_identifier_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!chainable_identifier_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "chainable_identifier_1", c)) break;
    }
    return true;
  }

  // DOT identifier
  private static boolean chainable_identifier_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chainable_identifier_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // visibility (
  // visibility | definition | constructor_def | var_dec | const_dec)*
  static boolean class_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = visibility(b, l + 1);
    r = r && class_body_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_class);
    return r;
  }

  // (
  // visibility | definition | constructor_def | var_dec | const_dec)*
  private static boolean class_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_body_1", c)) break;
    }
    return true;
  }

  // visibility | definition | constructor_def | var_dec | const_dec
  private static boolean class_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_1_0")) return false;
    boolean r;
    r = visibility(b, l + 1);
    if (!r) r = definition(b, l + 1);
    if (!r) r = constructor_def(b, l + 1);
    if (!r) r = var_dec(b, l + 1);
    if (!r) r = const_dec(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // CURLY_OPEN CURLY_CLOSE | CURLY_OPEN class_body CURLY_CLOSE
  public static boolean class_body_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, CURLY_OPEN, CURLY_CLOSE);
    if (!r) r = class_body_def_1(b, l + 1);
    exit_section_(b, m, CLASS_BODY_DEF, r);
    return r;
  }

  // CURLY_OPEN class_body CURLY_CLOSE
  private static boolean class_body_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_OPEN);
    r = r && class_body(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (ABSTRACT | STATIC)? CLASS IDENT (COLON chainable_identifier)? class_body_def
  public static boolean class_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_DEF, "<class def>");
    r = class_def_0(b, l + 1);
    r = r && consumeTokens(b, 1, CLASS, IDENT);
    p = r; // pin = 2
    r = r && report_error_(b, class_def_3(b, l + 1));
    r = p && class_body_def(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ABSTRACT | STATIC)?
  private static boolean class_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def_0")) return false;
    class_def_0_0(b, l + 1);
    return true;
  }

  // ABSTRACT | STATIC
  private static boolean class_def_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def_0_0")) return false;
    boolean r;
    r = consumeToken(b, ABSTRACT);
    if (!r) r = consumeToken(b, STATIC);
    return r;
  }

  // (COLON chainable_identifier)?
  private static boolean class_def_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def_3")) return false;
    class_def_3_0(b, l + 1);
    return true;
  }

  // COLON chainable_identifier
  private static boolean class_def_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && chainable_identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT (ASSIGN expr)?
  public static boolean closure(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closure")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLOSURE, null);
    r = consumeToken(b, IDENT);
    p = r; // pin = 1
    r = r && closure_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ASSIGN expr)?
  private static boolean closure_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closure_1")) return false;
    closure_1_0(b, l + 1);
    return true;
  }

  // ASSIGN expr
  private static boolean closure_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closure_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // closure (COMMA closure)*
  static boolean closure_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closure_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = closure(b, l + 1);
    r = r && closure_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_array);
    return r;
  }

  // (COMMA closure)*
  private static boolean closure_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closure_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!closure_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "closure_list_1", c)) break;
    }
    return true;
  }

  // COMMA closure
  private static boolean closure_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closure_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && closure(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BRACKET_OPEN BRACKET_CLOSE | BRACKET_OPEN closure_list BRACKET_CLOSE
  static boolean closures(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closures")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, BRACKET_OPEN, BRACKET_CLOSE);
    if (!r) r = closures_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BRACKET_OPEN closure_list BRACKET_CLOSE
  private static boolean closures_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closures_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && closure_list(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // shift_expr (comp_op comp_expr)*
  public static boolean comp_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comp_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, COMP_EXPR, "<comp expr>");
    r = shift_expr(b, l + 1);
    r = r && comp_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (comp_op comp_expr)*
  private static boolean comp_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comp_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comp_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "comp_expr_1", c)) break;
    }
    return true;
  }

  // comp_op comp_expr
  private static boolean comp_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comp_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = comp_op(b, l + 1);
    r = r && comp_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // GT | GEQ | LT | LEQ | TYPEOF
  public static boolean comp_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comp_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMP_OP, "<comp op>");
    r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, GEQ);
    if (!r) r = consumeToken(b, LT);
    if (!r) r = consumeToken(b, LEQ);
    if (!r) r = consumeToken(b, TYPEOF);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STATIC? CONST const_list SEMI
  public static boolean const_dec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_dec")) return false;
    if (!nextTokenIs(b, "<const dec>", CONST, STATIC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DEC, "<const dec>");
    r = const_dec_0(b, l + 1);
    r = r && consumeToken(b, CONST);
    p = r; // pin = 2
    r = r && report_error_(b, const_list(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // STATIC?
  private static boolean const_dec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_dec_0")) return false;
    consumeToken(b, STATIC);
    return true;
  }

  /* ********************************************************** */
  // single_const (COMMA single_const)*
  static boolean const_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = single_const(b, l + 1);
    r = r && const_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_stmt);
    return r;
  }

  // (COMMA single_const)*
  private static boolean const_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!const_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "const_list_1", c)) break;
    }
    return true;
  }

  // COMMA single_const
  private static boolean const_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && single_const(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OVERRIDDEN? CONSTRUCTOR params (COLON SUPER call)? block
  public static boolean constructor_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def")) return false;
    if (!nextTokenIs(b, "<constructor def>", CONSTRUCTOR, OVERRIDDEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR_DEF, "<constructor def>");
    r = constructor_def_0(b, l + 1);
    r = r && consumeToken(b, CONSTRUCTOR);
    p = r; // pin = 2
    r = r && report_error_(b, params(b, l + 1));
    r = p && report_error_(b, constructor_def_3(b, l + 1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // OVERRIDDEN?
  private static boolean constructor_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def_0")) return false;
    consumeToken(b, OVERRIDDEN);
    return true;
  }

  // (COLON SUPER call)?
  private static boolean constructor_def_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def_3")) return false;
    constructor_def_3_0(b, l + 1);
    return true;
  }

  // COLON SUPER call
  private static boolean constructor_def_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COLON, SUPER);
    r = r && call(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BRACKET_OPEN expr BRACKET_CLOSE
  public static boolean container_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "container_access")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTAINER_ACCESS, null);
    r = consumeToken(b, BRACKET_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && consumeToken(b, BRACKET_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // CONTINUE SEMI
  public static boolean continue_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continue_stmt")) return false;
    if (!nextTokenIs(b, CONTINUE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTINUE_STMT, null);
    r = consumeTokens(b, 1, CONTINUE, SEMI);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // function_def | namespace_def | class_def
  public static boolean definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DEFINITION, "<definition>");
    r = function_def(b, l + 1);
    if (!r) r = namespace_def(b, l + 1);
    if (!r) r = class_def(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // dictionary_entry (COMMA dictionary_entry)*
  static boolean dictionary_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = dictionary_entry(b, l + 1);
    r = r && dictionary_content_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_dictionary);
    return r;
  }

  // (COMMA dictionary_entry)*
  private static boolean dictionary_content_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_content_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!dictionary_content_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "dictionary_content_1", c)) break;
    }
    return true;
  }

  // COMMA dictionary_entry
  private static boolean dictionary_content_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_content_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && dictionary_entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr COLON expr
  public static boolean dictionary_entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_entry")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DICTIONARY_ENTRY, "<dictionary entry>");
    r = expr(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && expr(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // CURLY_OPEN CURLY_CLOSE | CURLY_OPEN dictionary_content CURLY_CLOSE
  public static boolean dictionary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_expr")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, CURLY_OPEN, CURLY_CLOSE);
    if (!r) r = dictionary_expr_1(b, l + 1);
    exit_section_(b, m, DICTIONARY_EXPR, r);
    return r;
  }

  // CURLY_OPEN dictionary_content CURLY_CLOSE
  private static boolean dictionary_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_OPEN);
    r = r && dictionary_content(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DO stmt WHILE expr SEMI
  public static boolean do_while(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_while")) return false;
    if (!nextTokenIs(b, DO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DO_WHILE, null);
    r = consumeToken(b, DO);
    p = r; // pin = 1
    r = r && report_error_(b, stmt(b, l + 1));
    r = p && report_error_(b, consumeToken(b, WHILE)) && r;
    r = p && report_error_(b, expr(b, l + 1)) && r;
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // comp_expr (eq_op eq_expr)*
  public static boolean eq_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eq_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, EQ_EXPR, "<eq expr>");
    r = comp_expr(b, l + 1);
    r = r && eq_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (eq_op eq_expr)*
  private static boolean eq_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eq_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!eq_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "eq_expr_1", c)) break;
    }
    return true;
  }

  // eq_op eq_expr
  private static boolean eq_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eq_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = eq_op(b, l + 1);
    r = r && eq_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EQUALS | NOT_EQUALS
  public static boolean eq_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eq_op")) return false;
    if (!nextTokenIs(b, "<eq op>", EQUALS, NOT_EQUALS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EQ_OP, "<eq op>");
    r = consumeToken(b, EQUALS);
    if (!r) r = consumeToken(b, NOT_EQUALS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // assign_expr
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, EXPR, "<expr>");
    r = assign_expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr SEMI
  static boolean expr_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_stmt")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = expr(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMI);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (BLOCK_COMMENT | COMMENT | definition | stmt)*
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!file_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file", c)) break;
    }
    exit_section_(b, l, m, true, false, TestParser::recover_global);
    return true;
  }

  // BLOCK_COMMENT | COMMENT | definition | stmt
  private static boolean file_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_0")) return false;
    boolean r;
    r = consumeToken(b, BLOCK_COMMENT);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = definition(b, l + 1);
    if (!r) r = stmt(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // FOR (VAR IDENT IN)? expr DO stmt
  public static boolean for_loop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_loop")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_LOOP, null);
    r = consumeToken(b, FOR);
    p = r; // pin = 1
    r = r && report_error_(b, for_loop_1(b, l + 1));
    r = p && report_error_(b, expr(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, DO)) && r;
    r = p && stmt(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (VAR IDENT IN)?
  private static boolean for_loop_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_loop_1")) return false;
    for_loop_1_0(b, l + 1);
    return true;
  }

  // VAR IDENT IN
  private static boolean for_loop_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_loop_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VAR, IDENT, IN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FROM chainable_identifier IMPORT chainable_identifier SEMI
  public static boolean from_import(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "from_import")) return false;
    if (!nextTokenIs(b, FROM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FROM_IMPORT, null);
    r = consumeToken(b, FROM);
    p = r; // pin = 1
    r = r && report_error_(b, chainable_identifier(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IMPORT)) && r;
    r = p && report_error_(b, chainable_identifier(b, l + 1)) && r;
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FROM chainable_identifier USE NAMESPACE chainable_identifier SEMI
  public static boolean from_use(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "from_use")) return false;
    if (!nextTokenIs(b, FROM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FROM_USE, null);
    r = consumeToken(b, FROM);
    p = r; // pin = 1
    r = r && report_error_(b, chainable_identifier(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, USE, NAMESPACE)) && r;
    r = p && report_error_(b, chainable_identifier(b, l + 1)) && r;
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (STATIC | OVERRIDDEN | NATIVE | ABSTRACT)? FUNCTION IDENT (SEMI | params block)
  public static boolean function_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DEF, "<function def>");
    r = function_def_0(b, l + 1);
    r = r && consumeTokens(b, 1, FUNCTION, IDENT);
    p = r; // pin = 2
    r = r && function_def_3(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (STATIC | OVERRIDDEN | NATIVE | ABSTRACT)?
  private static boolean function_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def_0")) return false;
    function_def_0_0(b, l + 1);
    return true;
  }

  // STATIC | OVERRIDDEN | NATIVE | ABSTRACT
  private static boolean function_def_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def_0_0")) return false;
    boolean r;
    r = consumeToken(b, STATIC);
    if (!r) r = consumeToken(b, OVERRIDDEN);
    if (!r) r = consumeToken(b, NATIVE);
    if (!r) r = consumeToken(b, ABSTRACT);
    return r;
  }

  // SEMI | params block
  private static boolean function_def_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    if (!r) r = function_def_3_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // params block
  private static boolean function_def_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def_3_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = params(b, l + 1);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // IF expr THEN stmt (ELSE stmt)?
  public static boolean if_else(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_else")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_ELSE, null);
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && report_error_(b, consumeToken(b, THEN)) && r;
    r = p && report_error_(b, stmt(b, l + 1)) && r;
    r = p && if_else_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ELSE stmt)?
  private static boolean if_else_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_else_4")) return false;
    if_else_4_0(b, l + 1);
    return true;
  }

  // ELSE stmt
  private static boolean if_else_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_else_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && stmt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IMPORT chainable_identifier SEMI
  public static boolean import_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_stmt")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_STMT, null);
    r = consumeToken(b, IMPORT);
    p = r; // pin = 1
    r = r && report_error_(b, chainable_identifier(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // INTEGER
  public static boolean integer_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_expr")) return false;
    if (!nextTokenIs(b, INTEGER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INTEGER);
    exit_section_(b, m, INTEGER_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // FUNCTION closures? params block
  public static boolean lambda_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_expr")) return false;
    if (!nextTokenIs(b, FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LAMBDA_EXPR, null);
    r = consumeToken(b, FUNCTION);
    p = r; // pin = 1
    r = r && report_error_(b, lambda_expr_1(b, l + 1));
    r = p && report_error_(b, params(b, l + 1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // closures?
  private static boolean lambda_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_expr_1")) return false;
    closures(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // bool_expr | integer_expr | real_expr | null_expr
  // | string_expr | this_expr | identifier | super_access | paren_expression
  static boolean literal_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_expr")) return false;
    boolean r;
    r = bool_expr(b, l + 1);
    if (!r) r = integer_expr(b, l + 1);
    if (!r) r = real_expr(b, l + 1);
    if (!r) r = null_expr(b, l + 1);
    if (!r) r = string_expr(b, l + 1);
    if (!r) r = this_expr(b, l + 1);
    if (!r) r = identifier(b, l + 1);
    if (!r) r = super_access(b, l + 1);
    if (!r) r = paren_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DOT IDENT
  public static boolean mem_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mem_access")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MEM_ACCESS, null);
    r = consumeTokens(b, 1, DOT, IDENT);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // pow_expr (mul_op mul_expr)*
  public static boolean mul_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, MUL_EXPR, "<mul expr>");
    r = pow_expr(b, l + 1);
    r = r && mul_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (mul_op mul_expr)*
  private static boolean mul_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mul_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mul_expr_1", c)) break;
    }
    return true;
  }

  // mul_op mul_expr
  private static boolean mul_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mul_op(b, l + 1);
    r = r && mul_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // MUL | DIV | IDIV | MOD
  public static boolean mul_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MUL_OP, "<mul op>");
    r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, DIV);
    if (!r) r = consumeToken(b, IDIV);
    if (!r) r = consumeToken(b, MOD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STATIC? NAMESPACE IDENT CURLY_OPEN definition* CURLY_CLOSE
  public static boolean namespace_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_def")) return false;
    if (!nextTokenIs(b, "<namespace def>", NAMESPACE, STATIC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMESPACE_DEF, "<namespace def>");
    r = namespace_def_0(b, l + 1);
    r = r && consumeTokens(b, 2, NAMESPACE, IDENT, CURLY_OPEN);
    p = r; // pin = 3
    r = r && report_error_(b, namespace_def_4(b, l + 1));
    r = p && consumeToken(b, CURLY_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // STATIC?
  private static boolean namespace_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_def_0")) return false;
    consumeToken(b, STATIC);
    return true;
  }

  // definition*
  private static boolean namespace_def_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_def_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "namespace_def_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SUB expr
  public static boolean negation_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negation_expr")) return false;
    if (!nextTokenIs(b, SUB)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SUB);
    r = r && expr(b, l + 1);
    exit_section_(b, m, NEGATION_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // NOT expr
  public static boolean not_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_expr")) return false;
    if (!nextTokenIs(b, NOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOT);
    r = r && expr(b, l + 1);
    exit_section_(b, m, NOT_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // NULL
  public static boolean null_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "null_expr")) return false;
    if (!nextTokenIs(b, NULL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NULL);
    exit_section_(b, m, NULL_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // xor_expr (OR or_expr)*
  public static boolean or_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, OR_EXPR, "<or expr>");
    r = xor_expr(b, l + 1);
    r = r && or_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OR or_expr)*
  private static boolean or_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!or_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "or_expr_1", c)) break;
    }
    return true;
  }

  // OR or_expr
  private static boolean or_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OR);
    r = r && or_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CONST? IDENT (ASSIGN expr)?
  public static boolean param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAM, "<param>");
    r = param_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENT));
    r = p && param_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CONST?
  private static boolean param_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_0")) return false;
    consumeToken(b, CONST);
    return true;
  }

  // (ASSIGN expr)?
  private static boolean param_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_2")) return false;
    param_2_0(b, l + 1);
    return true;
  }

  // ASSIGN expr
  private static boolean param_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // param (COMMA param)*
  static boolean param_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = param(b, l + 1);
    r = r && param_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_params);
    return r;
  }

  // (COMMA param)*
  private static boolean param_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!param_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "param_list_1", c)) break;
    }
    return true;
  }

  // COMMA param
  private static boolean param_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PAREN_OPEN PAREN_CLOSE | PAREN_OPEN param_list PAREN_CLOSE
  static boolean params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "params")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, PAREN_OPEN, PAREN_CLOSE);
    if (!r) r = params_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PAREN_OPEN param_list PAREN_CLOSE
  private static boolean params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "params_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PAREN_OPEN);
    r = r && param_list(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PAREN_OPEN expr PAREN_CLOSE
  static boolean paren_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expression")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PAREN_OPEN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // mul_expr (plus_op plus_expr)*
  public static boolean plus_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "plus_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, PLUS_EXPR, "<plus expr>");
    r = mul_expr(b, l + 1);
    r = r && plus_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (plus_op plus_expr)*
  private static boolean plus_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "plus_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!plus_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "plus_expr_1", c)) break;
    }
    return true;
  }

  // plus_op plus_expr
  private static boolean plus_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "plus_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = plus_op(b, l + 1);
    r = r && plus_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ADD | SUB
  public static boolean plus_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "plus_op")) return false;
    if (!nextTokenIs(b, "<plus op>", ADD, SUB)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PLUS_OP, "<plus op>");
    r = consumeToken(b, ADD);
    if (!r) r = consumeToken(b, SUB);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ADD expr
  public static boolean posivation_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "posivation_expr")) return false;
    if (!nextTokenIs(b, ADD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ADD);
    r = r && expr(b, l + 1);
    exit_section_(b, m, POSIVATION_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // range_expr (POW pow_expr)*
  public static boolean pow_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pow_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, POW_EXPR, "<pow expr>");
    r = range_expr(b, l + 1);
    r = r && pow_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (POW pow_expr)*
  private static boolean pow_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pow_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!pow_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pow_expr_1", c)) break;
    }
    return true;
  }

  // POW pow_expr
  private static boolean pow_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pow_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POW);
    r = r && pow_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // literal_expr | array_expr | dictionary_expr
  // | lambda_expr | typeof_prefix_expr | not_expr | negation_expr | posivation_expr
  static boolean primary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expr")) return false;
    boolean r;
    r = literal_expr(b, l + 1);
    if (!r) r = array_expr(b, l + 1);
    if (!r) r = dictionary_expr(b, l + 1);
    if (!r) r = lambda_expr(b, l + 1);
    if (!r) r = typeof_prefix_expr(b, l + 1);
    if (!r) r = not_expr(b, l + 1);
    if (!r) r = negation_expr(b, l + 1);
    if (!r) r = posivation_expr(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // unary_expr (COLON unary_expr)?
  public static boolean range_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, RANGE_EXPR, "<range expr>");
    r = unary_expr(b, l + 1);
    r = r && range_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COLON unary_expr)?
  private static boolean range_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expr_1")) return false;
    range_expr_1_0(b, l + 1);
    return true;
  }

  // COLON unary_expr
  private static boolean range_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && unary_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // REAL
  public static boolean real_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "real_expr")) return false;
    if (!nextTokenIs(b, REAL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REAL);
    exit_section_(b, m, REAL_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // !(BRACKET_CLOSE | COMMA | SEMI)
  static boolean recover_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_array")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_array_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BRACKET_CLOSE | COMMA | SEMI
  private static boolean recover_array_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_array_0")) return false;
    boolean r;
    r = consumeToken(b, BRACKET_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // !(COLON | PUBLIC | PROTECTED | PRIVATE | CONSTRUCTOR | CURLY_CLOSE | SEMI)
  static boolean recover_class(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_class")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_class_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COLON | PUBLIC | PROTECTED | PRIVATE | CONSTRUCTOR | CURLY_CLOSE | SEMI
  private static boolean recover_class_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_class_0")) return false;
    boolean r;
    r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, PROTECTED);
    if (!r) r = consumeToken(b, PRIVATE);
    if (!r) r = consumeToken(b, CONSTRUCTOR);
    if (!r) r = consumeToken(b, CURLY_CLOSE);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // !(CURLY_CLOSE | COMMA | SEMI)
  static boolean recover_dictionary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_dictionary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_dictionary_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CURLY_CLOSE | COMMA | SEMI
  private static boolean recover_dictionary_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_dictionary_0")) return false;
    boolean r;
    r = consumeToken(b, CURLY_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // !(SEMI | CURLY_CLOSE | FUNCTION | NATIVE | CLASS | NAMESPACE | VAR | CONST | CURLY_OPEN)
  static boolean recover_global(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_global")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_global_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMI | CURLY_CLOSE | FUNCTION | NATIVE | CLASS | NAMESPACE | VAR | CONST | CURLY_OPEN
  private static boolean recover_global_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_global_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, CURLY_CLOSE);
    if (!r) r = consumeToken(b, FUNCTION);
    if (!r) r = consumeToken(b, NATIVE);
    if (!r) r = consumeToken(b, CLASS);
    if (!r) r = consumeToken(b, NAMESPACE);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, CURLY_OPEN);
    return r;
  }

  /* ********************************************************** */
  // !(PAREN_CLOSE | COMMA | SEMI)
  static boolean recover_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_params")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_params_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PAREN_CLOSE | COMMA | SEMI
  private static boolean recover_params_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_params_0")) return false;
    boolean r;
    r = consumeToken(b, PAREN_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // !(SEMI | CURLY_CLOSE)
  static boolean recover_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_stmt_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMI | CURLY_CLOSE
  private static boolean recover_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_stmt_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, CURLY_CLOSE);
    return r;
  }

  /* ********************************************************** */
  // RETURN expr? SEMI
  public static boolean return_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_stmt")) return false;
    if (!nextTokenIs(b, RETURN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RETURN_STMT, null);
    r = consumeToken(b, RETURN);
    p = r; // pin = 1
    r = r && report_error_(b, return_stmt_1(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // expr?
  private static boolean return_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_stmt_1")) return false;
    expr(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // plus_expr (shift_op shift_expr)*
  public static boolean shift_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SHIFT_EXPR, "<shift expr>");
    r = plus_expr(b, l + 1);
    r = r && shift_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (shift_op shift_expr)*
  private static boolean shift_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!shift_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shift_expr_1", c)) break;
    }
    return true;
  }

  // shift_op shift_expr
  private static boolean shift_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = shift_op(b, l + 1);
    r = r && shift_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SAL | SAR | SLR
  public static boolean shift_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHIFT_OP, "<shift op>");
    r = consumeToken(b, SAL);
    if (!r) r = consumeToken(b, SAR);
    if (!r) r = consumeToken(b, SLR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENT ASSIGN expr
  public static boolean single_const(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_const")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SINGLE_CONST, null);
    r = consumeTokens(b, 2, IDENT, ASSIGN);
    p = r; // pin = 2
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IDENT (ASSIGN expr)?
  public static boolean single_var(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_var")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SINGLE_VAR, null);
    r = consumeToken(b, IDENT);
    p = r; // pin = 1
    r = r && single_var_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ASSIGN expr)?
  private static boolean single_var_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_var_1")) return false;
    single_var_1_0(b, l + 1);
    return true;
  }

  // ASSIGN expr
  private static boolean single_var_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_var_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // var_dec | const_dec | block | SEMI | if_else | while_do | do_while
  // | for_loop | try_catch | throw_stmt | break_stmt | continue_stmt | anon_function | return_stmt
  // | from_import | import_stmt | from_use | use_stmt | expr_stmt
  public static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STMT, "<stmt>");
    r = var_dec(b, l + 1);
    if (!r) r = const_dec(b, l + 1);
    if (!r) r = block(b, l + 1);
    if (!r) r = consumeToken(b, SEMI);
    if (!r) r = if_else(b, l + 1);
    if (!r) r = while_do(b, l + 1);
    if (!r) r = do_while(b, l + 1);
    if (!r) r = for_loop(b, l + 1);
    if (!r) r = try_catch(b, l + 1);
    if (!r) r = throw_stmt(b, l + 1);
    if (!r) r = break_stmt(b, l + 1);
    if (!r) r = continue_stmt(b, l + 1);
    if (!r) r = anon_function(b, l + 1);
    if (!r) r = return_stmt(b, l + 1);
    if (!r) r = from_import(b, l + 1);
    if (!r) r = import_stmt(b, l + 1);
    if (!r) r = from_use(b, l + 1);
    if (!r) r = use_stmt(b, l + 1);
    if (!r) r = expr_stmt(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // stmt*
  static boolean stmt_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_list")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!stmt(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "stmt_list", c)) break;
    }
    exit_section_(b, l, m, true, false, TestParser::recover_stmt);
    return true;
  }

  /* ********************************************************** */
  // STRING
  public static boolean string_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_expr")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    exit_section_(b, m, STRING_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // SUPER DOT IDENT
  public static boolean super_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "super_access")) return false;
    if (!nextTokenIs(b, SUPER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SUPER_ACCESS, null);
    r = consumeTokens(b, 1, SUPER, DOT, IDENT);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // THIS
  public static boolean this_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "this_expr")) return false;
    if (!nextTokenIs(b, THIS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, THIS);
    exit_section_(b, m, THIS_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // THROW expr SEMI
  public static boolean throw_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throw_stmt")) return false;
    if (!nextTokenIs(b, THROW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, THROW_STMT, null);
    r = consumeToken(b, THROW);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // TRY stmt CATCH VAR IDENT DO stmt
  public static boolean try_catch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_catch")) return false;
    if (!nextTokenIs(b, TRY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRY_CATCH, null);
    r = consumeToken(b, TRY);
    p = r; // pin = 1
    r = r && report_error_(b, stmt(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, CATCH, VAR, IDENT, DO)) && r;
    r = p && stmt(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // TYPEOF expr
  public static boolean typeof_prefix_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeof_prefix_expr")) return false;
    if (!nextTokenIs(b, TYPEOF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TYPEOF);
    r = r && expr(b, l + 1);
    exit_section_(b, m, TYPEOF_PREFIX_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // primary_expr (mem_access | container_access | call)*
  public static boolean unary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, UNARY_EXPR, "<unary expr>");
    r = primary_expr(b, l + 1);
    p = r; // pin = 1
    r = r && unary_expr_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (mem_access | container_access | call)*
  private static boolean unary_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!unary_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unary_expr_1", c)) break;
    }
    return true;
  }

  // mem_access | container_access | call
  private static boolean unary_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_1_0")) return false;
    boolean r;
    r = mem_access(b, l + 1);
    if (!r) r = container_access(b, l + 1);
    if (!r) r = call(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // USE NAMESPACE chainable_identifier SEMI
  public static boolean use_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "use_stmt")) return false;
    if (!nextTokenIs(b, USE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, USE_STMT, null);
    r = consumeTokens(b, 1, USE, NAMESPACE);
    p = r; // pin = 1
    r = r && report_error_(b, chainable_identifier(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // STATIC? VAR var_list SEMI
  public static boolean var_dec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec")) return false;
    if (!nextTokenIs(b, "<var dec>", STATIC, VAR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VAR_DEC, "<var dec>");
    r = var_dec_0(b, l + 1);
    r = r && consumeToken(b, VAR);
    p = r; // pin = 2
    r = r && report_error_(b, var_list(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // STATIC?
  private static boolean var_dec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec_0")) return false;
    consumeToken(b, STATIC);
    return true;
  }

  /* ********************************************************** */
  // single_var (COMMA single_var)*
  static boolean var_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = single_var(b, l + 1);
    r = r && var_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_stmt);
    return r;
  }

  // (COMMA single_var)*
  private static boolean var_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!var_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "var_list_1", c)) break;
    }
    return true;
  }

  // COMMA single_var
  private static boolean var_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && single_var(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (PUBLIC | PRIVATE | PROTECTED) COLON
  public static boolean visibility(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "visibility")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VISIBILITY, "<visibility>");
    r = visibility_0(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, COLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // PUBLIC | PRIVATE | PROTECTED
  private static boolean visibility_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "visibility_0")) return false;
    boolean r;
    r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, PRIVATE);
    if (!r) r = consumeToken(b, PROTECTED);
    return r;
  }

  /* ********************************************************** */
  // WHILE expr DO stmt
  public static boolean while_do(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_do")) return false;
    if (!nextTokenIs(b, WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_DO, null);
    r = consumeToken(b, WHILE);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && report_error_(b, consumeToken(b, DO)) && r;
    r = p && stmt(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // eq_expr (XOR xor_expr)*
  public static boolean xor_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, XOR_EXPR, "<xor expr>");
    r = eq_expr(b, l + 1);
    r = r && xor_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (XOR xor_expr)*
  private static boolean xor_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!xor_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "xor_expr_1", c)) break;
    }
    return true;
  }

  // XOR xor_expr
  private static boolean xor_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xor_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, XOR);
    r = r && xor_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}

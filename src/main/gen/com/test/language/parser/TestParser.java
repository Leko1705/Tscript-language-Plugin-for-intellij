// This is a generated file. Not intended for manual editing.
package com.test.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.test.language.psi.TestTypes.*;
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
    b = adapt_builder_(t, b, this, null);
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

  /* ********************************************************** */
  // CURLY_OPEN stmt_list CURLY_CLOSE
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_OPEN);
    r = r && stmt_list(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSE);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // modifiers (FUNCTION function_def | NAMESPACE namespace_def)
  static boolean definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = modifiers(b, l + 1);
    r = r && definition_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FUNCTION function_def | NAMESPACE namespace_def
  private static boolean definition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = definition_1_0(b, l + 1);
    if (!r) r = definition_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FUNCTION function_def
  private static boolean definition_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FUNCTION);
    r = r && function_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NAMESPACE namespace_def
  private static boolean definition_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAMESPACE);
    r = r && namespace_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (COMMENT | definition)*
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    while (true) {
      int c = current_position_(b);
      if (!file_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file", c)) break;
    }
    return true;
  }

  // COMMENT | definition
  private static boolean file_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_0")) return false;
    boolean r;
    r = consumeToken(b, COMMENT);
    if (!r) r = definition(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // IDENT params block
  public static boolean function_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && params(b, l + 1);
    r = r && block(b, l + 1);
    exit_section_(b, m, FUNCTION_DEF, r);
    return r;
  }

  /* ********************************************************** */
  // PUBLIC | PRIVATE | PROTECTED | NATIVE | OVERRIDDEN
  static boolean modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier")) return false;
    boolean r;
    r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, PRIVATE);
    if (!r) r = consumeToken(b, PROTECTED);
    if (!r) r = consumeToken(b, NATIVE);
    if (!r) r = consumeToken(b, OVERRIDDEN);
    return r;
  }

  /* ********************************************************** */
  // modifier*
  public static boolean modifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifiers")) return false;
    Marker m = enter_section_(b, l, _NONE_, MODIFIERS, "<modifiers>");
    while (true) {
      int c = current_position_(b);
      if (!modifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modifiers", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // IDENT CURLY_OPEN definition* CURLY_CLOSE
  public static boolean namespace_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_def")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENT, CURLY_OPEN);
    r = r && namespace_def_2(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSE);
    exit_section_(b, m, NAMESPACE_DEF, r);
    return r;
  }

  // definition*
  private static boolean namespace_def_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_def_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "namespace_def_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CONST? IDENT
  public static boolean param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAM, "<param>");
    r = param_0(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, IDENT);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CONST?
  private static boolean param_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_0")) return false;
    consumeToken(b, CONST);
    return true;
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
  public static boolean params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "params")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, PAREN_OPEN, PAREN_CLOSE);
    if (!r) r = params_1(b, l + 1);
    exit_section_(b, m, PARAMS, r);
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
  // !(PAREN_CLOSE | COMMA)
  static boolean recover_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_params")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_params_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PAREN_CLOSE | COMMA
  private static boolean recover_params_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_params_0")) return false;
    boolean r;
    r = consumeToken(b, PAREN_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // !(SEMI | CURLY_CLOSE)
  static boolean recover_semi_or_curly_open(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_semi_or_curly_open")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_semi_or_curly_open_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMI | CURLY_CLOSE
  private static boolean recover_semi_or_curly_open_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_semi_or_curly_open_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, CURLY_CLOSE);
    return r;
  }

  /* ********************************************************** */
  // var_dec | SEMI
  static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    if (!nextTokenIs(b, "", SEMI, VAR)) return false;
    boolean r;
    r = var_dec(b, l + 1);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // stmt*
  public static boolean stmt_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_list")) return false;
    Marker m = enter_section_(b, l, _NONE_, STMT_LIST, "<stmt list>");
    while (true) {
      int c = current_position_(b);
      if (!stmt(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "stmt_list", c)) break;
    }
    exit_section_(b, l, m, true, false, TestParser::recover_semi_or_curly_open);
    return true;
  }

  /* ********************************************************** */
  // VAR IDENT (ASSIGN NUM)? SEMI
  public static boolean var_dec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec")) return false;
    if (!nextTokenIs(b, VAR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VAR_DEC, null);
    r = consumeTokens(b, 1, VAR, IDENT);
    p = r; // pin = 1
    r = r && report_error_(b, var_dec_2(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ASSIGN NUM)?
  private static boolean var_dec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec_2")) return false;
    var_dec_2_0(b, l + 1);
    return true;
  }

  // ASSIGN NUM
  private static boolean var_dec_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ASSIGN, NUM);
    exit_section_(b, m, null, r);
    return r;
  }

}

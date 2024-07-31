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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARG, "<arg>");
    r = arg_0(b, l + 1);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // arg (COMMA arg)*
  static boolean arg_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = arg(b, l + 1);
    r = r && arg_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_params);
    return r;
  }

  // (COMMA arg)*
  private static boolean arg_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arg_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arg_list_1", c)) break;
    }
    return true;
  }

  // COMMA arg
  private static boolean arg_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && arg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BRACKET_OPEN BRACKET_CLOSE | BRACKET_OPEN array_content BRACKET_CLOSE
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, BRACKET_OPEN, BRACKET_CLOSE);
    if (!r) r = array_1(b, l + 1);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // BRACKET_OPEN array_content BRACKET_CLOSE
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && array_content(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
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
  // BREAK SEMI
  static boolean break_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_stmt")) return false;
    if (!nextTokenIs(b, BREAK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, BREAK, SEMI);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PAREN_OPEN PAREN_CLOSE | PAREN_OPEN arg_list PAREN_CLOSE
  public static boolean call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, PAREN_OPEN, PAREN_CLOSE);
    if (!r) r = call_1(b, l + 1);
    exit_section_(b, m, CALL, r);
    return r;
  }

  // PAREN_OPEN arg_list PAREN_CLOSE
  private static boolean call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PAREN_OPEN);
    r = r && arg_list(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CURLY_OPEN CURLY_CLOSE | CURLY_OPEN class_body_def CURLY_CLOSE
  static boolean class_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, CURLY_OPEN, CURLY_CLOSE);
    if (!r) r = class_body_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // CURLY_OPEN class_body_def CURLY_CLOSE
  private static boolean class_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_OPEN);
    r = r && class_body_def(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // visibility (
  // visibility | (OVERRIDDEN | STATIC)? definition | constructor_def | var_dec | cost_dec)*
  static boolean class_body_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = visibility(b, l + 1);
    r = r && class_body_def_1(b, l + 1);
    exit_section_(b, l, m, r, false, TestParser::recover_class);
    return r;
  }

  // (
  // visibility | (OVERRIDDEN | STATIC)? definition | constructor_def | var_dec | cost_dec)*
  private static boolean class_body_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_body_def_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_body_def_1", c)) break;
    }
    return true;
  }

  // visibility | (OVERRIDDEN | STATIC)? definition | constructor_def | var_dec | cost_dec
  private static boolean class_body_def_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    if (!r) r = class_body_def_1_0_1(b, l + 1);
    if (!r) r = constructor_def(b, l + 1);
    if (!r) r = var_dec(b, l + 1);
    if (!r) r = cost_dec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OVERRIDDEN | STATIC)? definition
  private static boolean class_body_def_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_body_def_1_0_1_0(b, l + 1);
    r = r && definition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OVERRIDDEN | STATIC)?
  private static boolean class_body_def_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def_1_0_1_0")) return false;
    class_body_def_1_0_1_0_0(b, l + 1);
    return true;
  }

  // OVERRIDDEN | STATIC
  private static boolean class_body_def_1_0_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_def_1_0_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OVERRIDDEN);
    if (!r) r = consumeToken(b, STATIC);
    return r;
  }

  /* ********************************************************** */
  // IDENT (COLON IDENT)? class_body
  public static boolean class_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && class_def_1(b, l + 1);
    r = r && class_body(b, l + 1);
    exit_section_(b, m, CLASS_DEF, r);
    return r;
  }

  // (COLON IDENT)?
  private static boolean class_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def_1")) return false;
    class_def_1_0(b, l + 1);
    return true;
  }

  // COLON IDENT
  private static boolean class_def_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_def_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COLON, IDENT);
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
  public static boolean closures(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "closures")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, BRACKET_OPEN, BRACKET_CLOSE);
    if (!r) r = closures_1(b, l + 1);
    exit_section_(b, m, CLOSURES, r);
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
  // CONSTRUCTOR params (COLON SUPER call)? block
  public static boolean constructor_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def")) return false;
    if (!nextTokenIs(b, CONSTRUCTOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR_DEF, null);
    r = consumeToken(b, CONSTRUCTOR);
    p = r; // pin = 1
    r = r && report_error_(b, params(b, l + 1));
    r = p && report_error_(b, constructor_def_2(b, l + 1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COLON SUPER call)?
  private static boolean constructor_def_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def_2")) return false;
    constructor_def_2_0(b, l + 1);
    return true;
  }

  // COLON SUPER call
  private static boolean constructor_def_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_def_2_0")) return false;
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
  static boolean continue_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continue_stmt")) return false;
    if (!nextTokenIs(b, CONTINUE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, CONTINUE, SEMI);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // CONST IDENT ASSIGN expr SEMI
  public static boolean cost_dec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cost_dec")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COST_DEC, null);
    r = consumeTokens(b, 1, CONST, IDENT, ASSIGN);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (NATIVE | ABSTRACT)? FUNCTION function_def | NAMESPACE namespace_def | ABSTRACT? CLASS class_def
  static boolean definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = definition_0(b, l + 1);
    if (!r) r = definition_1(b, l + 1);
    if (!r) r = definition_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (NATIVE | ABSTRACT)? FUNCTION function_def
  private static boolean definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = definition_0_0(b, l + 1);
    r = r && consumeToken(b, FUNCTION);
    r = r && function_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (NATIVE | ABSTRACT)?
  private static boolean definition_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0_0")) return false;
    definition_0_0_0(b, l + 1);
    return true;
  }

  // NATIVE | ABSTRACT
  private static boolean definition_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0_0_0")) return false;
    boolean r;
    r = consumeToken(b, NATIVE);
    if (!r) r = consumeToken(b, ABSTRACT);
    return r;
  }

  // NAMESPACE namespace_def
  private static boolean definition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAMESPACE);
    r = r && namespace_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ABSTRACT? CLASS class_def
  private static boolean definition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = definition_2_0(b, l + 1);
    r = r && consumeToken(b, CLASS);
    r = r && class_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ABSTRACT?
  private static boolean definition_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_2_0")) return false;
    consumeToken(b, ABSTRACT);
    return true;
  }

  /* ********************************************************** */
  // CURLY_OPEN CURLY_CLOSE | CURLY_OPEN dictionary_content CURLY_CLOSE
  public static boolean dictionary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, CURLY_OPEN, CURLY_CLOSE);
    if (!r) r = dictionary_1(b, l + 1);
    exit_section_(b, m, DICTIONARY, r);
    return r;
  }

  // CURLY_OPEN dictionary_content CURLY_CLOSE
  private static boolean dictionary_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_OPEN);
    r = r && dictionary_content(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // dictionary_entry (COMMA dictionary_entry)*
  public static boolean dictionary_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dictionary_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DICTIONARY_CONTENT, "<dictionary content>");
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
  // (literal | array | dictionary | lambda | super_access | typeof_prefix_expr) post_expr*
  static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr_0(b, l + 1);
    r = r && expr_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // literal | array | dictionary | lambda | super_access | typeof_prefix_expr
  private static boolean expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_0")) return false;
    boolean r;
    r = literal(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = dictionary(b, l + 1);
    if (!r) r = lambda(b, l + 1);
    if (!r) r = super_access(b, l + 1);
    if (!r) r = typeof_prefix_expr(b, l + 1);
    return r;
  }

  // post_expr*
  private static boolean expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!post_expr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_1", c)) break;
    }
    return true;
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
  // (COMMENT | definition | stmt)*
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

  // COMMENT | definition | stmt
  private static boolean file_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_0")) return false;
    boolean r;
    r = consumeToken(b, COMMENT);
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
  // IDENT params (SEMI | block)
  public static boolean function_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && params(b, l + 1);
    r = r && function_def_2(b, l + 1);
    exit_section_(b, m, FUNCTION_DEF, r);
    return r;
  }

  // SEMI | block
  private static boolean function_def_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_def_2")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = block(b, l + 1);
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
  // FUNCTION closures? params block
  public static boolean lambda(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda")) return false;
    if (!nextTokenIs(b, FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LAMBDA, null);
    r = consumeToken(b, FUNCTION);
    p = r; // pin = 1
    r = r && report_error_(b, lambda_1(b, l + 1));
    r = p && report_error_(b, params(b, l + 1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // closures?
  private static boolean lambda_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambda_1")) return false;
    closures(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TRUE | FALSE | INTEGER | REAL | NULL | STRING | THIS | IDENT
  static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, REAL);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, THIS);
    if (!r) r = consumeToken(b, IDENT);
    return r;
  }

  /* ********************************************************** */
  // DOT IDENT
  public static boolean member_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MEMBER_ACCESS, null);
    r = consumeTokens(b, 1, DOT, IDENT);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // container_access | member_access | call
  static boolean post_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "post_expr")) return false;
    boolean r;
    r = container_access(b, l + 1);
    if (!r) r = member_access(b, l + 1);
    if (!r) r = call(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // !(BRACKET_CLOSE | COMMA)
  static boolean recover_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_array")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_array_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BRACKET_CLOSE | COMMA
  private static boolean recover_array_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_array_0")) return false;
    boolean r;
    r = consumeToken(b, BRACKET_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
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
  // !(CURLY_CLOSE | COMMA)
  static boolean recover_dictionary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_dictionary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_dictionary_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CURLY_CLOSE | COMMA
  private static boolean recover_dictionary_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_dictionary_0")) return false;
    boolean r;
    r = consumeToken(b, CURLY_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // !(SEMI | CURLY_CLOSE | FUNCTION | NATIVE | CLASS | NAMESPACE | VAR | CONST)
  static boolean recover_global(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_global")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_global_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMI | CURLY_CLOSE | FUNCTION | NATIVE | CLASS | NAMESPACE | VAR | CONST
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
  // var_dec | cost_dec | block | SEMI | if_else | while_do | do_while | for_loop | try_catch | throw_stmt | break_stmt | continue_stmt | anon_function | expr_stmt
  static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    boolean r;
    r = var_dec(b, l + 1);
    if (!r) r = cost_dec(b, l + 1);
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
    if (!r) r = expr_stmt(b, l + 1);
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
    exit_section_(b, l, m, true, false, TestParser::recover_stmt);
    return true;
  }

  /* ********************************************************** */
  // SUPER DOT IDENT
  public static boolean super_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "super_access")) return false;
    if (!nextTokenIs(b, SUPER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SUPER, DOT, IDENT);
    exit_section_(b, m, SUPER_ACCESS, r);
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
  // VAR IDENT (ASSIGN expr)? SEMI
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

  // (ASSIGN expr)?
  private static boolean var_dec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec_2")) return false;
    var_dec_2_0(b, l + 1);
    return true;
  }

  // ASSIGN expr
  private static boolean var_dec_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_dec_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && expr(b, l + 1);
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

}

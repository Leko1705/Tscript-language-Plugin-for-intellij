package com.test.language.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.test.language.TestLexerAdapter;
import com.test.language.psi.TestTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class TestSyntaxHighlighter extends SyntaxHighlighterBase {

  public static final TextAttributesKey SEPARATOR =
      createTextAttributesKey("TEST_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);

  public static final TextAttributesKey IDENTIFIER =
          createTextAttributesKey("TEST_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

  public static final TextAttributesKey KEYWORD =
      createTextAttributesKey("TEST_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

  public static final TextAttributesKey STRING =
      createTextAttributesKey("TEST_VALUE", DefaultLanguageHighlighterColors.STRING);

  public static final TextAttributesKey LINE_COMMENT =
      createTextAttributesKey("TEST_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

  public static final TextAttributesKey BLOCK_COMMENT =
          createTextAttributesKey("TEST_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

  public static final TextAttributesKey BAD_CHARACTER =
      createTextAttributesKey("TEST_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

  public static final TextAttributesKey ERROR_MARK =
          createTextAttributesKey("TEST_ERROR_MARK", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES);

  public static final TextAttributesKey ERROR_UNDERLINE =
          createTextAttributesKey("TEST_ERROR_UNDERLINE", CodeInsightColors.ERRORS_ATTRIBUTES);

  public static final TextAttributesKey WARNING_UNDERLINE =
          createTextAttributesKey("TEST_WARNING_UNDERLINE", CodeInsightColors.WARNINGS_ATTRIBUTES);

  public static final TextAttributesKey WEAK_WARNING_UNDERLINE =
          createTextAttributesKey("TEST_WEAK_WARNING_UNDERLINE", CodeInsightColors.WEAK_WARNING_ATTRIBUTES);

  public static final TextAttributesKey NUMBER =
          createTextAttributesKey("TEST_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

  public static final TextAttributesKey SEMICOLON =
          createTextAttributesKey("TEST_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);

  public static final TextAttributesKey COMMA =
          createTextAttributesKey("TEST_COMMA", DefaultLanguageHighlighterColors.COMMA);

  public static final TextAttributesKey DOT =
          createTextAttributesKey("TEST_DOT", DefaultLanguageHighlighterColors.DOT);

  public static final TextAttributesKey PAREN =
          createTextAttributesKey("TEST_PAREN", DefaultLanguageHighlighterColors.PARENTHESES);

  public static final TextAttributesKey CURLY =
          createTextAttributesKey("TEST_CURLY", DefaultLanguageHighlighterColors.BRACES);


  public static final TextAttributesKey BRACKET =
          createTextAttributesKey("TEST_BRACKET", DefaultLanguageHighlighterColors.BRACKETS);

  public static final TextAttributesKey FUNC_DEF_NAME =
          createTextAttributesKey("TEST_FUNC_DEF_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);

  public static final TextAttributesKey CLASS_DEF_NAME =
          createTextAttributesKey("TEST_CLASS_DEF_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);

  public static final TextAttributesKey CLASS_REF_NAME =
          createTextAttributesKey("TEST_CLASS_REF_NAME", DefaultLanguageHighlighterColors.CLASS_REFERENCE);

  public static final TextAttributesKey MEMBER_REF_NAME =
          createTextAttributesKey("TEST_MEMBER_REF_NAME", DefaultLanguageHighlighterColors.INSTANCE_FIELD);

  public static final TextAttributesKey BUILTIN_REF_NAME =
          createTextAttributesKey("TEST_BUILTIN_REF_NAME", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);


  private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
  private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
  private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
  private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
  private static final TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[]{SEMICOLON};
  private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
  private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
  private static final TextAttributesKey[] DOT_KEYS = new TextAttributesKey[]{DOT};
  private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PAREN};
  private static final TextAttributesKey[] CURLY_KEYS = new TextAttributesKey[]{CURLY};
  private static final TextAttributesKey[] BRACKET_KEYS = new TextAttributesKey[]{BRACKET};

    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
  private static final TextAttributesKey[] LINE_COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT};
  private static final TextAttributesKey[] BLOCK_COMMENT_KEYS = new TextAttributesKey[]{BLOCK_COMMENT};
  private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new TestLexerAdapter();
  }

  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (isOperator(tokenType)) {
      return SEPARATOR_KEYS;
    }
    if (tokenType.equals(TestTypes.IDENT)){
      return IDENTIFIER_KEYS;
    }
    if (isKeyword(tokenType)) {
      return KEYWORD_KEYS;
    }
    if (tokenType.equals(TestTypes.STRING)) {
      return STRING_KEYS;
    }
    if (tokenType.equals(TestTypes.INTEGER) || tokenType.equals(TestTypes.REAL)){
      return NUMBER_KEYS;
    }
    if (tokenType.equals(TestTypes.COMMENT)) {
      return LINE_COMMENT_KEYS;
    }
    if (tokenType.equals(TestTypes.BLOCK_COMMENT)) {
      return BLOCK_COMMENT_KEYS;
    }
    if (tokenType.equals(TokenType.BAD_CHARACTER)) {
      return BAD_CHAR_KEYS;
    }
    if (tokenType.equals(TestTypes.SEMI)){
      return SEMICOLON_KEYS;
    }
    if (tokenType.equals(TestTypes.COMMA)){
      return COMMA_KEYS;
    }
    if (tokenType.equals(TestTypes.DOT)){
      return DOT_KEYS;
    }
    if (tokenType.equals(TestTypes.PAREN_OPEN) || tokenType.equals(TestTypes.PAREN_CLOSE)){
      return PAREN_KEYS;
    }
    if (tokenType.equals(TestTypes.CURLY_OPEN) || tokenType.equals(TestTypes.CURLY_CLOSE)){
      return CURLY_KEYS;
    }
    if (tokenType.equals(TestTypes.BRACKET_OPEN) || tokenType.equals(TestTypes.BRACKET_CLOSE)){
      return BRACKET_KEYS;
    }
    return EMPTY_KEYS;
  }

  private boolean isOperator(IElementType type){
    return Set.of(TestTypes.ADD, TestTypes.SUB,
            TestTypes.MUL, TestTypes.DIV, TestTypes.IDIV, TestTypes.MOD,
            TestTypes.POW, TestTypes.EQUALS, TestTypes.NOT_EQUALS,
            TestTypes.GT, TestTypes.GEQ, TestTypes.LT, TestTypes.LEQ,
            TestTypes.SAL, TestTypes.SAR, TestTypes.SLR,
            TestTypes.ADD_ASSIGN, TestTypes.SUB_ASSIGN,
            TestTypes.MUL_ASSIGN, TestTypes.DIV_ASSIGN,
            TestTypes.IDIV_ASSIGN, TestTypes.MOD_ASSIGN,
            TestTypes.POW_ASSIGN,
            TestTypes.SAL_ASSIGN, TestTypes.SAR_ASSIGN, TestTypes.SLR_ASSIGN).contains(type);
  }

  private static boolean isKeyword(IElementType type){
    return Set.of(
            TestTypes.PUBLIC, TestTypes.PROTECTED, TestTypes.PRIVATE,
            TestTypes.IF, TestTypes.THEN, TestTypes.ELSE,
            TestTypes.WHILE, TestTypes.DO,
            TestTypes.FOR, TestTypes.IN,
            TestTypes.VAR, TestTypes.CONST,
            TestTypes.FUNCTION, TestTypes.NATIVE, TestTypes.RETURN,
            TestTypes.CLASS, TestTypes.ABSTRACT,
            TestTypes.OVERRIDDEN,
            TestTypes.THIS, TestTypes.SUPER,
            TestTypes.AND, TestTypes.OR, TestTypes.XOR, TestTypes.NOT,
            TestTypes.BREAK, TestTypes.CONTINUE,
            TestTypes.TRY, TestTypes.CATCH,
            TestTypes.CONSTRUCTOR,
            TestTypes.TRUE, TestTypes.FALSE,
            TestTypes.NULL,
            TestTypes.THROW,
            TestTypes.STATIC,
            TestTypes.NAMESPACE,
            TestTypes.TYPEOF
    ).contains(type);
  }

}
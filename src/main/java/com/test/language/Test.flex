// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.test.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.test.language.psi.TestTypes;
import com.intellij.psi.TokenType;

%%

%class TestLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

WHITESPACE=[\s]+
COMMENT=#[^\r\n]*
IDENT=[a-zA-Z][a-zA-Z0-9]*

VAR=var
CONST=const

PUBLIC=public
PRIVATE=private
PROTECTED=protected
NATIVE=native
OVERRIDDEN=overridden

FUNCTION=function
NAMESPACE=namespace

SEMI=;
COMMA=,
CURLY_OPEN=\{
CURLY_CLOSE=\}
PAREN_OPEN=\(
PAREN_CLOSE=\)

ASSIGN==

NUM=\d+

%%

{NUM}                                                       { return TestTypes.NUM; }

{ASSIGN}                                                    { return TestTypes.ASSIGN; }
{SEMI}                                                      { return TestTypes.SEMI; }
{COMMA}                                                     { return TestTypes.COMMA; }
{CURLY_OPEN}                                                { return TestTypes.CURLY_OPEN; }
{CURLY_CLOSE}                                               { return TestTypes.CURLY_CLOSE; }
{PAREN_OPEN}                                                { return TestTypes.PAREN_OPEN; }
{PAREN_CLOSE}                                               { return TestTypes.PAREN_CLOSE; }

{VAR}                                                       { return TestTypes.VAR; }
{CONST}                                                     { return TestTypes.CONST; }

{FUNCTION}                                                   { return TestTypes.FUNCTION; }
{NAMESPACE}                                                  { return TestTypes.NAMESPACE; }

{PUBLIC}                                                     { return TestTypes.PUBLIC; }
{PRIVATE}                                                    { return TestTypes.PRIVATE; }
{PROTECTED}                                                  { return TestTypes.PROTECTED; }

{NATIVE}                                                     { return TestTypes.NATIVE; }
{OVERRIDDEN}                                                 { return TestTypes.OVERRIDDEN; }

{IDENT}                                                     { return TestTypes.IDENT; }
{WHITESPACE}|{COMMENT}+                                     { /* do nothing; just skip */ }

[^]                                                         { return TokenType.BAD_CHARACTER; }
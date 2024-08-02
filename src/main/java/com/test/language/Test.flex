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
ABSTRACT=abstract

FUNCTION=function
NAMESPACE=namespace
CLASS=class
CONSTRUCTOR=constructor

THIS=this
SUPER=super
STATIC=static

TYPEOF=typeof

IF=if
THEN=then
ELSE=else
WHILE=while
DO=do
FOR=for
IN=in
TRY=try
CATCH=catch
THROW=throw
BREAK=break
CONTINUE=continue

DOT=\.
SEMI=;
COMMA=,
COLON=:
CURLY_OPEN=\{
CURLY_CLOSE=\}
PAREN_OPEN=\(
PAREN_CLOSE=\)
BRACKET_OPEN=\[
BRACKET_CLOSE=\]

ADD_ASSIGN=\+=
SUB_ASSIGN=-=
MUL_ASSIGN=\*=
DIV_ASSIGN=\/=
IDIV_ASSIGN=\/\/=
MOD_ASSIGN=%=
POW_ASSIGN=\^=
SAL_ASSIGN=<<=
SAR_ASSIGN=>>=
SLR_ASSIGN=>>>=

AND=and
OR=or
XOR=xor
NOT=not

EQUALS===
NOT_EQUALS=\!=
GT=>
GEQ=>=
LT=<
LEQ=<=

SAL=<<
SAR=>>
SLR=>>>

ADD=\+
SUB=-
MUL=\*
DIV=\/
IDIV=\/\/
MOD=%
POW=\^

ASSIGN==

INTEGER=\d+
REAL=\d+\.\d*|\d*\.\d+
NULL=null
TRUE=true
FALSE=false
STRING=\"([^\"]*)\"

%%
{ADD_ASSIGN}                                                      { return TestTypes.ADD_ASSIGN; }
{SUB_ASSIGN}                                                      { return TestTypes.SUB_ASSIGN; }
{MUL_ASSIGN}                                                      { return TestTypes.MUL_ASSIGN; }
{DIV_ASSIGN}                                                      { return TestTypes.DIV_ASSIGN; }
{IDIV_ASSIGN}                                                      { return TestTypes.IDIV_ASSIGN; }
{MOD_ASSIGN}                                                      { return TestTypes.MOD_ASSIGN; }
{POW_ASSIGN}                                                      { return TestTypes.POW_ASSIGN; }
{SAL_ASSIGN}                                                      { return TestTypes.SAL_ASSIGN; }
{SAR_ASSIGN}                                                      { return TestTypes.SAR_ASSIGN; }
{SLR_ASSIGN}                                                      { return TestTypes.SLR_ASSIGN; }

{AND}                                                      { return TestTypes.AND; }
{OR}                                                      { return TestTypes.OR; }
{XOR}                                                      { return TestTypes.XOR; }
{NOT}                                                      { return TestTypes.NOT; }

{EQUALS}                                                      { return TestTypes.EQUALS; }
{NOT_EQUALS}                                                      { return TestTypes.NOT_EQUALS; }
{GT}                                                      { return TestTypes.GT; }
{GEQ}                                                      { return TestTypes.GEQ; }
{LT}                                                      { return TestTypes.LT; }
{LEQ}                                                      { return TestTypes.LEQ; }

{SAL}                                                      { return TestTypes.SAL; }
{SAR}                                                      { return TestTypes.SAR; }
{SLR}                                                      { return TestTypes.SLR; }

{ADD}                                                      { return TestTypes.ADD; }
{SUB}                                                      { return TestTypes.SUB; }
{MUL}                                                      { return TestTypes.MUL; }
{DIV}                                                      { return TestTypes.DIV; }
{IDIV}                                                      { return TestTypes.IDIV; }
{MOD}                                                      { return TestTypes.MOD; }
{POW}                                                      { return TestTypes.POW; }


{REAL}                                                      { return TestTypes.REAL; }
{INTEGER}                                                   { return TestTypes.INTEGER; }
{NULL}                                                      { return TestTypes.NULL; }
{TRUE}                                                      { return TestTypes.TRUE; }
{FALSE}                                                     { return TestTypes.FALSE; }
{STRING}                                                    { return TestTypes.STRING; }

{IF}                                                        { return TestTypes.IF; }
{THEN}                                                      { return TestTypes.THEN; }
{ELSE}                                                      { return TestTypes.ELSE; }

{WHILE}                                                     { return TestTypes.WHILE; }
{DO}                                                        { return TestTypes.DO; }
{FOR}                                                       { return TestTypes.FOR; }
{IN}                                                        { return TestTypes.IN; }
{BREAK}                                                     { return TestTypes.BREAK; }
{CONTINUE}                                                  { return TestTypes.CONTINUE; }

{TRY}                                                       { return TestTypes.TRY; }
{CATCH}                                                     { return TestTypes.CATCH; }
{THROW}                                                     { return TestTypes.THROW; }

{DOT}                                                       { return TestTypes.DOT; }
{ASSIGN}                                                    { return TestTypes.ASSIGN; }
{SEMI}                                                      { return TestTypes.SEMI; }
{COMMA}                                                     { return TestTypes.COMMA; }
{COLON}                                                     { return TestTypes.COLON; }

{CURLY_OPEN}                                                { return TestTypes.CURLY_OPEN; }
{CURLY_CLOSE}                                               { return TestTypes.CURLY_CLOSE; }
{PAREN_OPEN}                                                { return TestTypes.PAREN_OPEN; }
{PAREN_CLOSE}                                               { return TestTypes.PAREN_CLOSE; }
{BRACKET_OPEN}                                              { return TestTypes.BRACKET_OPEN; }
{BRACKET_CLOSE}                                             { return TestTypes.BRACKET_CLOSE; }

{VAR}                                                       { return TestTypes.VAR; }
{CONST}                                                     { return TestTypes.CONST; }

{FUNCTION}                                                   { return TestTypes.FUNCTION; }
{NAMESPACE}                                                  { return TestTypes.NAMESPACE; }
{CLASS}                                                      { return TestTypes.CLASS; }
{CONSTRUCTOR}                                                { return TestTypes.CONSTRUCTOR; }
{SUPER}                                                      { return TestTypes.SUPER; }
{THIS}                                                        { return TestTypes.THIS; }
{STATIC}                                                      { return TestTypes.STATIC; }

{TYPEOF}                                                      { return TestTypes.TYPEOF; }


{PUBLIC}                                                     { return TestTypes.PUBLIC; }
{PRIVATE}                                                    { return TestTypes.PRIVATE; }
{PROTECTED}                                                  { return TestTypes.PROTECTED; }

{NATIVE}                                                     { return TestTypes.NATIVE; }
{OVERRIDDEN}                                                 { return TestTypes.OVERRIDDEN; }
{ABSTRACT}                                                   { return TestTypes.ABSTRACT; }

{IDENT}                                                     { return TestTypes.IDENT; }
{WHITESPACE}|{COMMENT}+                                     { return TokenType.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }
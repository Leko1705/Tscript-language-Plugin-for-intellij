package com.tscript.ide.psi;

import com.intellij.psi.tree.TokenSet;

public interface TestTokenSets {

    TokenSet IDENTIFIERS = TokenSet.create(TestTypes.IDENT);

    TokenSet COMMENTS = TokenSet.create(TestTypes.COMMENT);

}

// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestClassDef extends com.test.language.psi.MixinElements.TestClassDef {

  @Nullable
  TestChainableIdentifier getChainableIdentifier();

  @Nullable
  TestClassBodyDef getClassBodyDef();

  String getName();

  @Nullable PsiElement getNameIdentifier();

  TestChainableIdentifier getSuper();

  PsiElement getAbstractElement();

  PsiElement getStaticElement();

}

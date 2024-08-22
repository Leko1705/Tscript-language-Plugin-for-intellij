// This is a generated file. Not intended for manual editing.
package com.tscript.ide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tscript.ide.psi.MixinElements.TestNSpaceDef;

public interface TestNamespaceDef extends TestNSpaceDef {

  @NotNull
  List<TestDefinition> getDefinitionList();

  String getName();

  @Nullable PsiElement getNameIdentifier();

  PsiElement getStaticElement();

}

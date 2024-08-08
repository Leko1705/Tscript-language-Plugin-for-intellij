// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.test.language.psi.TestNamedElements.SuperMemAccess;
import com.intellij.util.IncorrectOperationException;

public interface TestSuperAccess extends SuperMemAccess {

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  @Nullable PsiElement getNameIdentifier();

}

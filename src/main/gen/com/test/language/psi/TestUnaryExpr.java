// This is a generated file. Not intended for manual editing.
package com.test.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TestUnaryExpr extends TestExpr {

  @NotNull
  List<TestCall> getCallList();

  @NotNull
  List<TestContainerAccess> getContainerAccessList();

  @Nullable
  TestExpr getExpr();

  @Nullable
  TestIdentifier getIdentifier();

  @NotNull
  List<TestMemAccess> getMemAccessList();

  @Nullable
  TestSuperAccess getSuperAccess();

}

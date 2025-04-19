// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactIfStatement extends TactStatement {

  @Nullable
  TactElseBranch getElseBranch();

  @NotNull
  List<TactElseIfBranch> getElseIfBranchList();

  @Nullable
  TactExpression getExpression();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getIf();

  //WARNING: isGuard(...) is skipped
  //matching isGuard(TactIfStatement, ...)
  //methods are not found in TactPsiImplUtil

}

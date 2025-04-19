// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactTernaryExpr extends TactExpression {

  @NotNull
  List<TactExpression> getExpressionList();

  @Nullable
  PsiElement getColon();

  @NotNull
  PsiElement getQuestion();

  @NotNull
  TactExpression getCondition();

  @Nullable
  TactExpression getThenBranch();

  @Nullable
  TactExpression getElseBranch();

}

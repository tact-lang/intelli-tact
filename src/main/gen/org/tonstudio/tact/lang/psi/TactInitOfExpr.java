// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactInitOfExpr extends TactExpression {

  @Nullable
  TactArgumentList getArgumentList();

  @Nullable
  TactTypeReferenceExpression getTypeReferenceExpression();

  @NotNull
  PsiElement getInitOf();

  @Nullable List<@NotNull TactFieldOrParameter> resolve();

}

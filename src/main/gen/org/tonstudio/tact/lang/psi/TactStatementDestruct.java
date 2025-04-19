// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactStatementDestruct extends TactCompositeElement {

  @NotNull
  List<TactDestructItem> getDestructItemList();

  @NotNull
  List<TactExpression> getExpressionList();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getSemicolon();

  @NotNull
  PsiElement getLet();

}

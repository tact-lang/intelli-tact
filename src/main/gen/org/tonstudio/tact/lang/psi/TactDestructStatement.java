// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactDestructStatement extends TactStatement {

  @NotNull
  List<TactDestructItem> getDestructItemList();

  @Nullable
  TactExpression getExpression();

  @NotNull
  TactTypeReferenceExpression getTypeReferenceExpression();

  @Nullable
  TactSemi getSemi();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @NotNull
  PsiElement getLet();

}

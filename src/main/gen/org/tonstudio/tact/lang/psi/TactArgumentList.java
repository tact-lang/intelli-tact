// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactArgumentList extends TactCompositeElement {

  @NotNull
  List<TactExpression> getExpressionList();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}

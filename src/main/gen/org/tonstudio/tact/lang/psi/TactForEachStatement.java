// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactForEachStatement extends TactStatement {

  @Nullable
  TactExpression getExpression();

  @NotNull
  List<TactVarDefinition> getVarDefinitionList();

  @Nullable
  PsiElement getComma();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getForeach();

  @Nullable
  PsiElement getIn();

  @Nullable
  TactVarDefinition getKey();

  @Nullable
  TactVarDefinition getValue();

}

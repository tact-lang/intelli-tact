// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactCatchClause extends TactCompositeElement {

  @Nullable
  TactBlock getBlock();

  @Nullable
  TactVarDefinition getVarDefinition();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getCatch();

}

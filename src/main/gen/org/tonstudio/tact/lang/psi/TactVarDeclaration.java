// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactVarDeclaration extends TactCompositeElement {

  @Nullable
  TactExpression getExpression();

  @Nullable
  TactTypeHint getTypeHint();

  @Nullable
  TactVarDefinition getVarDefinition();

  @Nullable
  PsiElement getAssign();

  @Nullable
  PsiElement getSemicolon();

  @NotNull
  PsiElement getLet();

}

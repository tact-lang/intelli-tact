// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactConstDeclaration extends TactCompositeElement {

  @Nullable
  TactConstDefinition getConstDefinition();

  @NotNull
  List<TactConstantModifier> getConstantModifierList();

  @Nullable
  PsiElement getSemicolon();

  @NotNull
  PsiElement getConst();

}

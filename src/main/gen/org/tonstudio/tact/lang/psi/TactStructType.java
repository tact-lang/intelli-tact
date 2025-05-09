// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactStructType extends TactType, TactFieldListOwner {

  @NotNull
  List<TactFieldDefinition> getFieldDefinitionList();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getStruct();

  @NotNull List<@NotNull TactFieldDefinition> getFieldList();

}

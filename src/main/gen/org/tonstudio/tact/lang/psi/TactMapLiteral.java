// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactMapLiteral extends TactExpression {

  @NotNull
  List<TactMapEntry> getMapEntryList();

  @NotNull
  TactMapType getMapType();

  @NotNull
  PsiElement getLbrace();

  @NotNull
  PsiElement getRbrace();

}

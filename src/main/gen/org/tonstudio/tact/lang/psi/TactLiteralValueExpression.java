// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactLiteralValueExpression extends TactExpression {

  @NotNull
  List<TactElement> getElementList();

  @NotNull
  TactType getType();

  @NotNull
  PsiElement getLbrace();

  @NotNull
  PsiElement getRbrace();

}

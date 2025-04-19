// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactLiteral extends TactExpression {

  @Nullable
  PsiElement getBin();

  @Nullable
  PsiElement getFalse();

  @Nullable
  PsiElement getHex();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getNull();

  @Nullable
  PsiElement getOct();

  @Nullable
  PsiElement getTrue();

  boolean isNumeric();

  boolean isBoolean();

  int intValue();

}

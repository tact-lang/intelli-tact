// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactUntilStatement extends TactStatement {

  @Nullable
  TactExpression getExpression();

  @Nullable
  TactSemi getSemi();

  @NotNull
  PsiElement getDo();

  @Nullable
  PsiElement getUntil();

}

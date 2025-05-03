// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactElement extends TactCompositeElement {

  @Nullable
  TactExpression getExpression();

  @Nullable
  TactKey getKey();

  @Nullable
  TactValue getValue();

  @Nullable
  PsiElement getColon();

}

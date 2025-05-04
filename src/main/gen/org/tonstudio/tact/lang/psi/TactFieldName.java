// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import kotlin.Pair;

public interface TactFieldName extends TactReferenceExpressionBase {

  @NotNull
  TactReferenceExpression getReferenceExpression();

  @NotNull PsiElement getIdentifier();

  @Nullable TactCompositeElement getQualifier();

  @Nullable PsiElement resolve();

  @NotNull Pair<@NotNull Integer, @NotNull Integer> getIdentifierBounds();

}

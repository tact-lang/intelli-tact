// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector.Access;
import kotlin.Pair;
import org.tonstudio.tact.lang.psi.impl.TactReference;

public interface TactReferenceExpression extends TactExpression, TactReferenceExpressionBase {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  TactReference getReference();

  @Nullable
  TactCompositeElement getQualifier();

  @NotNull
  Access getReadWriteAccess();

  @NotNull
  Pair<Integer, Integer> getIdentifierBounds();

  @Nullable
  PsiElement resolve();

}

// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import kotlin.Pair;

public interface TactCallExpr extends TactExpression {

  @NotNull
  TactArgumentList getArgumentList();

  @Nullable
  TactDummyRightHandRule getDummyRightHandRule();

  @Nullable
  TactExpression getExpression();

  @NotNull
  List<TactExpression> getArguments();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  TactReferenceExpression getQualifier();

  @Nullable
  PsiElement resolve();

  int paramIndexOf(@NotNull PsiElement pos);

  @Nullable
  Pair<TactSignature, TactSignatureOwner> resolveSignature();

}

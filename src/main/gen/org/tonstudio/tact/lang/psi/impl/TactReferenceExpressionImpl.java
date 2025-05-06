// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector.Access;
import kotlin.Pair;

public class TactReferenceExpressionImpl extends TactExpressionImpl implements TactReferenceExpression {

  public TactReferenceExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitReferenceExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  public @NotNull TactReference getReference() {
    return TactPsiImplUtil.getReference(this);
  }

  @Override
  public @Nullable TactCompositeElement getQualifier() {
    return TactPsiImplUtil.getQualifier(this);
  }

  @Override
  public @NotNull Access getReadWriteAccess() {
    return TactPsiImplUtil.getReadWriteAccess(this);
  }

  @Override
  public @NotNull Pair<@NotNull Integer, @NotNull Integer> getIdentifierBounds() {
    return TactPsiImplUtil.getIdentifierBounds(this);
  }

  @Override
  public @Nullable PsiElement resolve() {
    return TactPsiImplUtil.resolve(this);
  }

}

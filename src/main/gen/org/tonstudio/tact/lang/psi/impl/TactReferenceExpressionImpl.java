// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
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
  @NotNull
  public TactReference getReference() {
    return TactPsiImplUtil.getReference(this);
  }

  @Override
  @Nullable
  public TactCompositeElement getQualifier() {
    return TactPsiImplUtil.getQualifier(this);
  }

  @Override
  @NotNull
  public Access getReadWriteAccess() {
    return TactPsiImplUtil.getReadWriteAccess(this);
  }

  @Override
  @NotNull
  public Pair<Integer, Integer> getIdentifierBounds() {
    return TactPsiImplUtil.getIdentifierBounds(this);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    return TactPsiImplUtil.resolve(this);
  }

}

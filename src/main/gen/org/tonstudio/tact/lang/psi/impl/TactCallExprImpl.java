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
import kotlin.Pair;

public class TactCallExprImpl extends TactExpressionImpl implements TactCallExpr {

  public TactCallExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitCallExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactArgumentList getArgumentList() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactArgumentList.class));
  }

  @Override
  @Nullable
  public TactDummyRightHandRule getDummyRightHandRule() {
    return TactPsiTreeUtil.getChildOfType(this, TactDummyRightHandRule.class);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return TactPsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  @NotNull
  public List<TactExpression> getArguments() {
    return TactPsiImplUtil.getArguments(this);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  @Nullable
  public TactReferenceExpression getQualifier() {
    return TactPsiImplUtil.getQualifier(this);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    return TactPsiImplUtil.resolve(this);
  }

  @Override
  public int paramIndexOf(@NotNull PsiElement pos) {
    return TactPsiImplUtil.paramIndexOf(this, pos);
  }

  @Override
  @Nullable
  public Pair<TactSignature, TactSignatureOwner> resolveSignature() {
    return TactPsiImplUtil.resolveSignature(this);
  }

}

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
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactArgumentList.class));
  }

  @Override
  @Nullable
  public TactDummyRightHandRule getDummyRightHandRule() {
    return PsiTreeUtil.getChildOfType(this, TactDummyRightHandRule.class);
  }

  @Override
  @Nullable
  public TactExpression getExpression() {
    return PsiTreeUtil.getChildOfType(this, TactExpression.class);
  }

  @Override
  public @NotNull List<@NotNull TactExpression> getArguments() {
    return TactPsiImplUtil.getArguments(this);
  }

  @Override
  public @Nullable PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @Nullable TactReferenceExpression getQualifier() {
    return TactPsiImplUtil.getQualifier(this);
  }

  @Override
  public @Nullable PsiElement resolve() {
    return TactPsiImplUtil.resolve(this);
  }

  @Override
  public int paramIndexOf(@NotNull PsiElement pos) {
    return TactPsiImplUtil.paramIndexOf(this, pos);
  }

  @Override
  public @Nullable Pair<@NotNull TactSignature, @NotNull TactSignatureOwner> resolveSignature() {
    return TactPsiImplUtil.resolveSignature(this);
  }

}

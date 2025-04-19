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

public class TactFieldNameImpl extends TactCompositeElementImpl implements TactFieldName {

  public TactFieldNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitFieldName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactReferenceExpression getReferenceExpression() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactReferenceExpression.class));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  @Nullable
  public TactCompositeElement getQualifier() {
    return TactPsiImplUtil.getQualifier(this);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    return TactPsiImplUtil.resolve(this);
  }

  @Override
  @NotNull
  public Pair<Integer, Integer> getIdentifierBounds() {
    return TactPsiImplUtil.getIdentifierBounds(this);
  }

}

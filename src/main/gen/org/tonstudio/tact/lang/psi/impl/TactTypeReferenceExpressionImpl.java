// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactTypeReferenceExpressionStub;
import org.tonstudio.tact.lang.psi.*;
import kotlin.Pair;
import com.intellij.psi.stubs.IStubElementType;

public class TactTypeReferenceExpressionImpl extends TactStubbedElementImpl<TactTypeReferenceExpressionStub> implements TactTypeReferenceExpression {

  public TactTypeReferenceExpressionImpl(@NotNull TactTypeReferenceExpressionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactTypeReferenceExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitTypeReferenceExpression(this);
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
  public @Nullable PsiElement resolve() {
    return TactPsiImplUtil.resolve(this);
  }

  @Override
  public @NotNull Pair<@NotNull Integer, @NotNull Integer> getIdentifierBounds() {
    return TactPsiImplUtil.getIdentifierBounds(this);
  }

}

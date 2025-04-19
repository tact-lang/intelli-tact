// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactTypeStub;
import org.tonstudio.tact.lang.psi.*;
import kotlin.Pair;
import com.intellij.psi.stubs.IStubElementType;

public class TactTypeImpl extends TactStubbedElementImpl<TactTypeStub> implements TactType {

  public TactTypeImpl(@NotNull TactTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactType getType() {
    return TactPsiTreeUtil.getStubChildOfType(this, TactType.class);
  }

  @Override
  @Nullable
  public TactTypeExtra getTypeExtra() {
    return TactPsiTreeUtil.getChildOfType(this, TactTypeExtra.class);
  }

  @Override
  @Nullable
  public TactTypeReferenceExpression getTypeReferenceExpression() {
    return TactPsiTreeUtil.getStubChildOfType(this, TactTypeReferenceExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public Pair<TactType, TactTypeExtra> resolveType() {
    return TactPsiImplUtil.resolveType(this);
  }

}

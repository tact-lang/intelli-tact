// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactFieldDefinitionStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class TactFieldDefinitionImpl extends TactNamedElementImpl<TactFieldDefinitionStub> implements TactFieldDefinition {

  public TactFieldDefinitionImpl(@NotNull TactFieldDefinitionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactFieldDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitFieldDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactDefaultFieldValue getDefaultFieldValue() {
    return PsiTreeUtil.getChildOfType(this, TactDefaultFieldValue.class);
  }

  @Override
  @Nullable
  public TactType getType() {
    return PsiTreeUtil.getStubChildOfType(this, TactType.class);
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return PsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @Nullable
  public PsiElement getColon() {
    return findChildByType(COLON);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  public @Nullable TactCompositeElement getQualifier() {
    return TactPsiImplUtil.getQualifier(this);
  }

  @Override
  public @Nullable String getQualifiedName() {
    return TactPsiImplUtil.getQualifiedName(this);
  }

  @Override
  public @NotNull TactTypeEx getTypeInner(@Nullable ResolveState context) {
    return TactPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  public @NotNull TactNamedElement getOwner() {
    return TactPsiImplUtil.getOwner(this);
  }

}

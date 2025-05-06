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
import com.intellij.psi.stubs.IStubElementType;
import org.tonstudio.tact.lang.stubs.TactTypeStub;

public class TactMessageTypeImpl extends TactTypeImpl implements TactMessageType {

  public TactMessageTypeImpl(@NotNull TactTypeStub<?> stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactMessageTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitMessageType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactFieldDefinition> getFieldDefinitionList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactFieldDefinition.class);
  }

  @Override
  @Nullable
  public TactIdent getIdent() {
    return PsiTreeUtil.getChildOfType(this, TactIdent.class);
  }

  @Override
  @Nullable
  public TactMessageId getMessageId() {
    return PsiTreeUtil.getChildOfType(this, TactMessageId.class);
  }

  @Override
  @Nullable
  public PsiElement getLbrace() {
    return findChildByType(LBRACE);
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  public @Nullable PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @NotNull List<@NotNull TactFieldDefinition> getFieldList() {
    return TactPsiImplUtil.getFieldList(this);
  }

}

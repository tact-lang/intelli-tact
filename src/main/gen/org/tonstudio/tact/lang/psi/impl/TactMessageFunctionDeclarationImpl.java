// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactMessageFunctionDeclarationStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactMessageFunctionDeclarationImpl extends TactNamedElementImpl<TactMessageFunctionDeclarationStub> implements TactMessageFunctionDeclaration {

  public TactMessageFunctionDeclarationImpl(@NotNull TactMessageFunctionDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactMessageFunctionDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitMessageFunctionDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, TactBlock.class);
  }

  @Override
  @NotNull
  public TactMessageKind getMessageKind() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactMessageKind.class));
  }

  @Override
  @Nullable
  public TactParameters getParameters() {
    return PsiTreeUtil.getStubChildOfType(this, TactParameters.class);
  }

  @Override
  @Nullable
  public TactReceiveStringId getReceiveStringId() {
    return PsiTreeUtil.getChildOfType(this, TactReceiveStringId.class);
  }

  @Override
  public @Nullable PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @NotNull String getName() {
    return TactPsiImplUtil.getName(this);
  }

  @Override
  public @NotNull String nameLike() {
    return TactPsiImplUtil.nameLike(this);
  }

}

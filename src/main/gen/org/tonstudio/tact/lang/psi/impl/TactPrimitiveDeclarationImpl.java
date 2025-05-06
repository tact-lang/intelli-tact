// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactPrimitiveDeclarationStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class TactPrimitiveDeclarationImpl extends TactNamedElementImpl<TactPrimitiveDeclarationStub> implements TactPrimitiveDeclaration {

  public TactPrimitiveDeclarationImpl(@NotNull TactPrimitiveDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactPrimitiveDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitPrimitiveDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactPrimitiveType getPrimitiveType() {
    return notNullChild(PsiTreeUtil.getStubChildOfType(this, TactPrimitiveType.class));
  }

  @Override
  @NotNull
  public PsiElement getSemicolon() {
    return notNullChild(findChildByType(SEMICOLON));
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
  public @NotNull TactTypeEx getTypeInner(@Nullable ResolveState context) {
    return TactPsiImplUtil.getTypeInner(this, context);
  }

}

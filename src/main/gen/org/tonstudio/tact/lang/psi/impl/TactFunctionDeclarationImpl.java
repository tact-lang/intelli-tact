// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactFunctionDeclarationStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.ResolveState;
import kotlin.Pair;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class TactFunctionDeclarationImpl extends TactNamedElementImpl<TactFunctionDeclarationStub> implements TactFunctionDeclaration {

  public TactFunctionDeclarationImpl(@NotNull TactFunctionDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactFunctionDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitFunctionDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactAttributes getAttributes() {
    return PsiTreeUtil.getStubChildOfType(this, TactAttributes.class);
  }

  @Override
  @Nullable
  public TactBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, TactBlock.class);
  }

  @Override
  @NotNull
  public List<TactFunctionAttribute> getFunctionAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TactFunctionAttribute.class);
  }

  @Override
  @NotNull
  public TactSignature getSignature() {
    return notNullChild(PsiTreeUtil.getStubChildOfType(this, TactSignature.class));
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return PsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @NotNull
  public PsiElement getFun() {
    return notNullChild(findChildByType(FUN));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  public @NotNull String getName() {
    return TactPsiImplUtil.getName(this);
  }

  @Override
  public @Nullable TactTypeEx getTypeInner(@Nullable ResolveState context) {
    return TactPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  public boolean isGet() {
    return TactPsiImplUtil.isGet(this);
  }

  @Override
  public boolean isAbstract() {
    return TactPsiImplUtil.isAbstract(this);
  }

  @Override
  public boolean isVirtual() {
    return TactPsiImplUtil.isVirtual(this);
  }

  @Override
  public @NotNull Pair<@NotNull String, @NotNull Boolean> computeMethodId() {
    return TactPsiImplUtil.computeMethodId(this);
  }

}

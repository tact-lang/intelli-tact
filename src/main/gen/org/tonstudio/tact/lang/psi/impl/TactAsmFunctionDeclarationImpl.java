// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactAsmFunctionDeclarationStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.tonstudio.tact.lang.psi.types.TactTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class TactAsmFunctionDeclarationImpl extends TactNamedElementImpl<TactAsmFunctionDeclarationStub> implements TactAsmFunctionDeclaration {

  public TactAsmFunctionDeclarationImpl(@NotNull TactAsmFunctionDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactAsmFunctionDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAsmFunctionDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactAsmFunctionBody getAsmFunctionBody() {
    return PsiTreeUtil.getChildOfType(this, TactAsmFunctionBody.class);
  }

  @Override
  @NotNull
  public TactAsmHeader getAsmHeader() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactAsmHeader.class));
  }

  @Override
  @NotNull
  public List<TactFunctionAttribute> getFunctionAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TactFunctionAttribute.class);
  }

  @Override
  @Nullable
  public TactSignature getSignature() {
    return PsiTreeUtil.getStubChildOfType(this, TactSignature.class);
  }

  @Override
  @Nullable
  public PsiElement getFun() {
    return findChildByType(FUN);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  public @Nullable TactTypeEx getTypeInner(@Nullable ResolveState context) {
    return TactPsiImplUtil.getTypeInner(this, context);
  }

}

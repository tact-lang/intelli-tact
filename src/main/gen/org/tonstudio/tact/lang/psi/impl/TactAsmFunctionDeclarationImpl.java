// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
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
  @NotNull
  public TactAsmHeader getAsmHeader() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactAsmHeader.class));
  }

  @Override
  @NotNull
  public List<TactAsmInstruction> getAsmInstructionList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactAsmInstruction.class);
  }

  @Override
  @NotNull
  public List<TactFunctionAttribute> getFunctionAttributeList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactFunctionAttribute.class);
  }

  @Override
  @Nullable
  public TactSignature getSignature() {
    return TactPsiTreeUtil.getStubChildOfType(this, TactSignature.class);
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
  @Nullable
  public TactTypeEx getTypeInner(@Nullable ResolveState context) {
    return TactPsiImplUtil.getTypeInner(this, context);
  }

}

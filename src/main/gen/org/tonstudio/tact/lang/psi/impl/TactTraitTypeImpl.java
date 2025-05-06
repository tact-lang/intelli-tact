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

public class TactTraitTypeImpl extends TactTypeImpl implements TactTraitType {

  public TactTraitTypeImpl(@NotNull TactTypeStub<?> stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactTraitTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitTraitType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactConstDeclaration> getConstDeclarationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactConstDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactContractInitDeclaration> getContractInitDeclarationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactContractInitDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactFieldDefinition> getFieldDefinitionList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactFieldDefinition.class);
  }

  @Override
  @NotNull
  public List<TactFunctionDeclaration> getFunctionDeclarationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactMessageFunctionDeclaration> getMessageFunctionDeclarationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactMessageFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public TactWithClause getWithClause() {
    return PsiTreeUtil.getStubChildOfType(this, TactWithClause.class);
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
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getTrait() {
    return notNullChild(findChildByType(TRAIT));
  }

  @Override
  public @NotNull List<@NotNull TactFieldDefinition> getFieldList() {
    return TactPsiImplUtil.getFieldList(this);
  }

  @Override
  public @NotNull List<@NotNull TactFunctionDeclaration> getMethodsList() {
    return TactPsiImplUtil.getMethodsList(this);
  }

  @Override
  public @NotNull List<@NotNull TactConstDeclaration> getConstantsList() {
    return TactPsiImplUtil.getConstantsList(this);
  }

  @Override
  public @NotNull List<@NotNull TactTraitDeclaration> getInheritedTraits() {
    return TactPsiImplUtil.getInheritedTraits(this);
  }

}

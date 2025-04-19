// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import org.tonstudio.tact.lang.stubs.TactTypeStub;

public class TactContractTypeImpl extends TactTypeImpl implements TactContractType {

  public TactContractTypeImpl(@NotNull TactTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactContractTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitContractType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactConstDeclaration> getConstDeclarationList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactConstDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactContractInitDeclaration> getContractInitDeclarationList() {
    return TactPsiTreeUtil.getStubChildrenOfTypeAsList(this, TactContractInitDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactFieldDeclaration> getFieldDeclarationList() {
    return TactPsiTreeUtil.getStubChildrenOfTypeAsList(this, TactFieldDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactFunctionDeclaration> getFunctionDeclarationList() {
    return TactPsiTreeUtil.getStubChildrenOfTypeAsList(this, TactFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public List<TactMessageFunctionDeclaration> getMessageFunctionDeclarationList() {
    return TactPsiTreeUtil.getStubChildrenOfTypeAsList(this, TactMessageFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public TactWithClause getWithClause() {
    return TactPsiTreeUtil.getChildOfType(this, TactWithClause.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @NotNull
  public PsiElement getRbrace() {
    return notNullChild(findChildByType(RBRACE));
  }

  @Override
  @NotNull
  public PsiElement getContract() {
    return notNullChild(findChildByType(CONTRACT));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  @NotNull
  public List<TactFieldDefinition> getFieldList() {
    return TactPsiImplUtil.getFieldList(this);
  }

  @Override
  @NotNull
  public List<TactFunctionDeclaration> getMethodsList() {
    return TactPsiImplUtil.getMethodsList(this);
  }

  @Override
  @NotNull
  public List<TactConstDefinition> getConstantsList() {
    return TactPsiImplUtil.getConstantsList(this);
  }

}

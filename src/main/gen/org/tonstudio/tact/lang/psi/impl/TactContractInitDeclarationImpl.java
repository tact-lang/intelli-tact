// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactContractInitDeclarationStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactContractInitDeclarationImpl extends TactNamedElementImpl<TactContractInitDeclarationStub> implements TactContractInitDeclaration {

  public TactContractInitDeclarationImpl(@NotNull TactContractInitDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactContractInitDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitContractInitDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactBlock getBlock() {
    return TactPsiTreeUtil.getChildOfType(this, TactBlock.class);
  }

  @Override
  @Nullable
  public TactParameters getParameters() {
    return TactPsiTreeUtil.getStubChildOfType(this, TactParameters.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return TactPsiImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public String getName() {
    return TactPsiImplUtil.getName(this);
  }

}

// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactSignatureStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactSignatureImpl extends TactStubbedElementImpl<TactSignatureStub> implements TactSignature {

  public TactSignatureImpl(@NotNull TactSignatureStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactSignatureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitSignature(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactParameters getParameters() {
    return notNullChild(PsiTreeUtil.getStubChildOfType(this, TactParameters.class));
  }

  @Override
  @Nullable
  public TactResult getResult() {
    return PsiTreeUtil.getStubChildOfType(this, TactResult.class);
  }

  @Override
  public boolean withSelf() {
    return TactPsiImplUtil.withSelf(this);
  }

}

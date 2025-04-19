// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.tonstudio.tact.lang.psi.TactPsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactPlainAttributeStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactPlainAttributeImpl extends TactStubbedElementImpl<TactPlainAttributeStub> implements TactPlainAttribute {

  public TactPlainAttributeImpl(@NotNull TactPlainAttributeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactPlainAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitPlainAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactArgumentList getArgumentList() {
    return TactPsiTreeUtil.getChildOfType(this, TactArgumentList.class);
  }

  @Override
  @NotNull
  public TactAttributeKey getAttributeKey() {
    return notNullChild(TactPsiTreeUtil.getStubChildOfType(this, TactAttributeKey.class));
  }

}

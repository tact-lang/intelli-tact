// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.tonstudio.tact.lang.TactTypes.*;
import org.tonstudio.tact.lang.stubs.TactAttributeStub;
import org.tonstudio.tact.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class TactAttributeImpl extends TactStubbedElementImpl<TactAttributeStub> implements TactAttribute {

  public TactAttributeImpl(@NotNull TactAttributeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactArgumentList getArgumentList() {
    return PsiTreeUtil.getChildOfType(this, TactArgumentList.class);
  }

  @Override
  @NotNull
  public PsiElement getAt() {
    return notNullChild(findChildByType(AT));
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

}

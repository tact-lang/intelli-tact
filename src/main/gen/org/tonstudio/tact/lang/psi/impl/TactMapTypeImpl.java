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

public class TactMapTypeImpl extends TactTypeImpl implements TactMapType {

  public TactMapTypeImpl(@NotNull TactTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TactMapTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitMapType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactType> getTypeList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, TactType.class);
  }

  @Override
  @Nullable
  public PsiElement getComma() {
    return findChildByType(COMMA);
  }

  @Override
  @Nullable
  public PsiElement getGreater() {
    return findChildByType(GREATER);
  }

  @Override
  @Nullable
  public PsiElement getLess() {
    return findChildByType(LESS);
  }

  @Override
  @Nullable
  public TactType getKeyType() {
    List<TactType> p1 = getTypeList();
    return p1.size() < 1 ? null : p1.get(0);
  }

  @Override
  @Nullable
  public TactType getValueType() {
    List<TactType> p1 = getTypeList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}

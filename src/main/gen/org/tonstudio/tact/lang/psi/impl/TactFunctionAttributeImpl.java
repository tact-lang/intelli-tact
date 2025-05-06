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

public class TactFunctionAttributeImpl extends TactCompositeElementImpl implements TactFunctionAttribute {

  public TactFunctionAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitFunctionAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactGetAttribute getGetAttribute() {
    return PsiTreeUtil.getChildOfType(this, TactGetAttribute.class);
  }

  @Override
  @Nullable
  public PsiElement getAbstract() {
    return findChildByType(ABSTRACT);
  }

  @Override
  @Nullable
  public PsiElement getExtends() {
    return findChildByType(EXTENDS);
  }

  @Override
  @Nullable
  public PsiElement getInline() {
    return findChildByType(INLINE);
  }

  @Override
  @Nullable
  public PsiElement getMutates() {
    return findChildByType(MUTATES);
  }

  @Override
  @Nullable
  public PsiElement getOverride() {
    return findChildByType(OVERRIDE);
  }

  @Override
  @Nullable
  public PsiElement getVirtual() {
    return findChildByType(VIRTUAL);
  }

}

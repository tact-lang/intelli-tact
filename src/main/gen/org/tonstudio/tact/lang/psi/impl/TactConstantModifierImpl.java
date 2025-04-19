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

public class TactConstantModifierImpl extends TactCompositeElementImpl implements TactConstantModifier {

  public TactConstantModifierImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitConstantModifier(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getAbstract() {
    return findChildByType(ABSTRACT);
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

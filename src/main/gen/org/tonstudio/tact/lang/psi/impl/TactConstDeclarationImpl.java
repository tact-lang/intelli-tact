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

public class TactConstDeclarationImpl extends TactCompositeElementImpl implements TactConstDeclaration {

  public TactConstDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitConstDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactConstDefinition getConstDefinition() {
    return TactPsiTreeUtil.getChildOfType(this, TactConstDefinition.class);
  }

  @Override
  @NotNull
  public List<TactConstantModifier> getConstantModifierList() {
    return TactPsiTreeUtil.getChildrenOfTypeAsList(this, TactConstantModifier.class);
  }

  @Override
  @Nullable
  public TactSemi getSemi() {
    return TactPsiTreeUtil.getChildOfType(this, TactSemi.class);
  }

  @Override
  @NotNull
  public PsiElement getConst() {
    return notNullChild(findChildByType(CONST));
  }

}

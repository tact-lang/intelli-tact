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

public class TactMessageKindImpl extends TactCompositeElementImpl implements TactMessageKind {

  public TactMessageKindImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitMessageKind(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getExternal() {
    return findChildByType(EXTERNAL);
  }

  @Override
  @Nullable
  public PsiElement getReceive() {
    return findChildByType(RECEIVE);
  }

}

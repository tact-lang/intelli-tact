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

public class TactAsmBocHexImpl extends TactCompositeElementImpl implements TactAsmBocHex {

  public TactAsmBocHexImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAsmBocHex(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getBocLiteral() {
    return findChildByType(BOC_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getEmptyBocLiteral() {
    return findChildByType(EMPTY_BOC_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getToBocFift() {
    return findChildByType(TO_BOC_FIFT);
  }

}

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

public class TactMapLiteralImpl extends TactExpressionImpl implements TactMapLiteral {

  public TactMapLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitMapLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TactMapEntry> getMapEntryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TactMapEntry.class);
  }

  @Override
  @NotNull
  public TactMapType getMapType() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, TactMapType.class));
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @NotNull
  public PsiElement getRbrace() {
    return notNullChild(findChildByType(RBRACE));
  }

}

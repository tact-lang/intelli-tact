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
import com.intellij.psi.PsiReference;

public class TactStringLiteralImpl extends TactExpressionImpl implements TactStringLiteral {

  public TactStringLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitStringLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getClosingQuote() {
    return notNullChild(findChildByType(CLOSING_QUOTE));
  }

  @Override
  @NotNull
  public PsiElement getOpenQuote() {
    return notNullChild(findChildByType(OPEN_QUOTE));
  }

  @Override
  public boolean isValidHost() {
    return TactPsiImplUtil.isValidHost(this);
  }

  @Override
  public @NotNull TactStringLiteral updateText(@NotNull String text) {
    return TactPsiImplUtil.updateText(this, text);
  }

  @Override
  public @NotNull StringLiteralEscaper<@NotNull TactStringLiteral> createLiteralTextEscaper() {
    return TactPsiImplUtil.createLiteralTextEscaper(this);
  }

  @Override
  public @NotNull PsiReference @NotNull [] getReferences() {
    return TactPsiImplUtil.getReferences(this);
  }

  @Override
  public @NotNull String getContents() {
    return TactPsiImplUtil.getContents(this);
  }

}

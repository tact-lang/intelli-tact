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

public class TactAsmExpressionImpl extends TactCompositeElementImpl implements TactAsmExpression {

  public TactAsmExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitAsmExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TactAsmArguments getAsmArguments() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactAsmArguments.class));
  }

  @Override
  @NotNull
  public TactAsmInstruction getAsmInstruction() {
    return notNullChild(TactPsiTreeUtil.getChildOfType(this, TactAsmInstruction.class));
  }

}

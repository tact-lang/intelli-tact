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

public class TactInstanceArgumentImpl extends TactCompositeElementImpl implements TactInstanceArgument {

  public TactInstanceArgumentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TactVisitor visitor) {
    visitor.visitInstanceArgument(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TactVisitor) accept((TactVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TactInstanceArgumentFull getInstanceArgumentFull() {
    return PsiTreeUtil.getChildOfType(this, TactInstanceArgumentFull.class);
  }

  @Override
  @Nullable
  public TactInstanceArgumentShort getInstanceArgumentShort() {
    return PsiTreeUtil.getChildOfType(this, TactInstanceArgumentShort.class);
  }

}

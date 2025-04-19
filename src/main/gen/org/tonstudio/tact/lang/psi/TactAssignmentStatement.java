// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TactAssignmentStatement extends TactStatement {

  @NotNull
  TactAssignOp getAssignOp();

  @NotNull
  List<TactExpression> getExpressionList();

  @NotNull
  TactExpression getLeft();

  @Nullable
  TactExpression getRight();

}

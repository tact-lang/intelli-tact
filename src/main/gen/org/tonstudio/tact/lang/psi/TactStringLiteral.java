// This is a generated file. Not intended for manual editing.
package org.tonstudio.tact.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiReference;

public interface TactStringLiteral extends TactExpression, PsiLanguageInjectionHost {

  @NotNull
  PsiElement getClosingQuote();

  @NotNull
  PsiElement getOpenQuote();

  boolean isValidHost();

  @NotNull TactStringLiteral updateText(@NotNull String text);

  @NotNull StringLiteralEscaper<@NotNull TactStringLiteral> createLiteralTextEscaper();

  //WARNING: getDecodedText(...) is skipped
  //matching getDecodedText(TactStringLiteral, ...)
  //methods are not found in TactPsiImplUtil

  @NotNull PsiReference @NotNull [] getReferences();

  @NotNull String getContents();

}

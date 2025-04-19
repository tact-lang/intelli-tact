package org.tonstudio.tact.lang.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.tonstudio.tact.lang.TactLanguage
import org.tonstudio.tact.lang.TactTypes.*

class TactFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            TactFormatterBlock(
                formattingContext.node,
                null,
                null,
                null,
                createSpacing(formattingContext.codeStyleSettings)
            ),
            formattingContext.codeStyleSettings
        )
    }

    private fun createSpacing(codeStyleSettings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(codeStyleSettings, TactLanguage)
            .between(LPAREN, RPAREN).spacing(0, 0, 0, false, 0)
            .between(LBRACE, RBRACE).spacing(0, 0, 0, false, 0)
            .between(LBRACK, RBRACK).spacing(0, 0, 0, false, 0)
            .after(RBRACE).spaces(1)
    }
}

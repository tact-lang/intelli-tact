package org.tonstudio.tact.lang

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import org.tonstudio.tact.lang.TactTypes.*

object TactParserUtil : GeneratedParserUtilBase() {
    @JvmStatic
    fun keyOrValueExpression(builder: PsiBuilder, level: Int): Boolean {
        val m = enter_section_(builder)
        var r = TactParser.Expression(builder, level + 1, -1)

        if (!r) {
            r = TactParser.LiteralValueExpression(builder, level + 1)
        }

        val type = if (r && builder.tokenType === COLON) KEY else VALUE
        exit_section_(builder, m, type, r)
        return r
    }
}

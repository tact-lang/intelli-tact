package org.tonstudio.tact.lang

import com.intellij.lexer.LayeredLexer
import org.tonstudio.tact.lang.lexer.TactLexer

class TactHighlightingLexer : LayeredLexer(TactLexer())

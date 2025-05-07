package org.tonstudio.tact.ide.colors

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.psi.codeStyle.DisplayPriority
import com.intellij.psi.codeStyle.DisplayPrioritySortable
import org.tonstudio.tact.ide.ui.Icons
import org.tonstudio.tact.lang.TactSyntaxHighlighter

class TactColorsSettingsPage : ColorSettingsPage, DisplayPrioritySortable {
    object Util {
        val ANNOTATOR_TAGS: Map<String, TextAttributesKey> = TactColor.entries.associateBy({ it.name }, { it.textAttributesKey })
        val ATTRS: Array<AttributesDescriptor> = TactColor.entries.map { it.attributesDescriptor }.toTypedArray()
    }

    override fun getHighlighter() = TactSyntaxHighlighter()
    override fun getIcon() = Icons.Tact
    override fun getAttributeDescriptors() = Util.ATTRS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getAdditionalHighlightingTagToDescriptorMap() = Util.ANNOTATOR_TAGS
    override fun getDisplayName() = "Tact"

    override fun getDemoText() = """
        // <LINE_COMMENT>This is a line comment</LINE_COMMENT>
        /* <BLOCK_COMMENT>This is a block comment</BLOCK_COMMENT> */

        <DOC_COMMENT>/// <DOC_COMMENT>Documentation comment example</DOC_COMMENT>
        /// <DOC_HEADING># Heading 1</DOC_HEADING>
        /// Use <DOC_LINK>[Link](https://example.com)</DOC_LINK>,
        /// <DOC_EMPHASIS>*italic*</DOC_EMPHASIS> and <DOC_STRONG>**bold**</DOC_STRONG>
        /// with <DOC_CODE>`inline code`</DOC_CODE>
        /// and <DOC_CODE>```block code```</DOC_CODE>
        /// <COMMENT_REFERENCE>ReferenceTag</COMMENT_REFERENCE></DOC_COMMENT>
        const <CONSTANT>VALUE</CONSTANT>: <PRIMITIVE>Int</PRIMITIVE> = 314;

        struct <STRUCT>Point</STRUCT> {
            <FIELD>x</FIELD><OPERATOR>:</OPERATOR> <PRIMITIVE>Int</PRIMITIVE>;
            <FIELD>y</FIELD><OPERATOR>:</OPERATOR> <PRIMITIVE>Int</PRIMITIVE>;
        }

        <KEYWORD>message</KEYWORD>(0x1) <MESSAGE>Ping</MESSAGE> {}

        inline fun <FUNCTION>sum</FUNCTION>(<PARAMETER>a</PARAMETER>: <PRIMITIVE>Int</PRIMITIVE>, <PARAMETER>b</PARAMETER>: <PRIMITIVE>Int</PRIMITIVE>): <PRIMITIVE>Int</PRIMITIVE> {
            return <PARAMETER>a</PARAMETER> <OPERATOR>+</OPERATOR> <PARAMETER>b</PARAMETER>;
        }

        <ATTRIBUTE>@name</ATTRIBUTE>(<STRING>"get_data"</STRING>)
        native <NATIVE_FUNCTION>getData</NATIVE_FUNCTION>();

        <KEYWORD>asm</KEYWORD> fun <ASM_FUNCTION>doSomethingSpecial</ASM_FUNCTION>() {
            <ASM_STACK_ELEMENT>s0</ASM_STACK_ELEMENT> <ASM_INSTRUCTION>PUSH</ASM_INSTRUCTION>
            <ASM_CONTROL_REGISTER>c0</ASM_CONTROL_REGISTER> <ASM_INSTRUCTION>PUSH</ASM_INSTRUCTION>
            2 <ASM_INSTRUCTION>DROP</ASM_INSTRUCTION>
        }

        contract <CONTRACT>MyContract</CONTRACT> {
            receive(<PARAMETER>msg</PARAMETER><OPERATOR>:</OPERATOR> <MESSAGE>Ping</MESSAGE>) {
                <FUNCTION>reply</FUNCTION>(<PARAMETER>msg</PARAMETER>)
            }
        }

        trait <TRAIT>Serializable</TRAIT> {
            fun <FUNCTION>toCell</FUNCTION>(): <PRIMITIVE>Cell</PRIMITIVE>;
        }

        fun <FUNCTION>main</FUNCTION>() {
            let <VARIABLE>greeting</VARIABLE>: <PRIMITIVE>String</PRIMITIVE> = <STRING>"Hello<VALID_STRING_ESCAPE>\\n</VALID_STRING_ESCAPE>World"</STRING>;
            let <VARIABLE>optional</VARIABLE>: <PRIMITIVE>Int</PRIMITIVE>? = null;
            <VARIABLE>optional</VARIABLE><NOT_NULL_OPERATOR>!!</NOT_NULL_OPERATOR>;
            let <MUTABLE_VARIABLE>counter</MUTABLE_VARIABLE>: <PRIMITIVE>Int</PRIMITIVE> = 0;
            if (true) { <MUTABLE_VARIABLE>counter</MUTABLE_VARIABLE> += 1 }
            let <VARIABLE>p</VARIABLE>: <STRUCT>Point</STRUCT> = <STRUCT>Point</STRUCT> { <FIELD>x</FIELD><OPERATOR>:</OPERATOR> 0, <FIELD>y</FIELD><OPERATOR>:</OPERATOR> 0 };
            <FUNCTION>dump</FUNCTION>(<STRING>"Point initialized"</STRING>);
        }
    """.trimIndent()

    override fun getPriority() = DisplayPriority.KEY_LANGUAGE_SETTINGS
}

package org.tonstudio.tact.lang.stubs

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.NamedStubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.tonstudio.tact.lang.psi.TactNamedElement

abstract class TactNamedStub<T : TactNamedElement> : NamedStubBase<T> {
    val isExported: Boolean

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isExported: Boolean,
    ) : super(parent, elementType, name) {
        this.isExported = isExported
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isExported: Boolean,
    ) : super(parent, elementType, name) {
        this.isExported = isExported
    }

    override fun toString(): String {
        val name = name
        val str = super.toString()
        return if (StringUtil.isEmpty(name)) str else "$str: $name"
    }
}

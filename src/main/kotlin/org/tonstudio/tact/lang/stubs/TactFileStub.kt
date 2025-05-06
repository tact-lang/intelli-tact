package org.tonstudio.tact.lang.stubs

import com.intellij.psi.stubs.PsiFileStubImpl
import org.tonstudio.tact.lang.TactFileElementType
import org.tonstudio.tact.lang.psi.TactFile

class TactFileStub(file: TactFile?) : PsiFileStubImpl<TactFile?>(file) {
    override fun getType() = TactFileElementType.INSTANCE
}

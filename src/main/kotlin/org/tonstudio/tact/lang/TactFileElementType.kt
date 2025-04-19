package org.tonstudio.tact.lang

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import org.tonstudio.tact.lang.psi.TactFile
import org.tonstudio.tact.lang.psi.TactTokenTypes.COMMENTS
import org.tonstudio.tact.lang.stubs.TactFileStub
import org.tonstudio.tact.lang.stubs.index.TactModulesFingerprintIndex
import org.tonstudio.tact.lang.stubs.index.TactModulesIndex

class TactFileElementType : IStubFileElementType<TactFileStub>("TACT_FILE", TactLanguage) {
    override fun getStubVersion() = VERSION

    override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
        override fun createStubForFile(file: PsiFile): StubElement<*> {
            if (file !is TactFile) {
                return super.createStubForFile(file)
            }

            return TactFileStub(file)
        }

        override fun skipChildProcessingWhenBuildingStubs(parent: ASTNode, node: ASTNode): Boolean {
            return node.elementType == TactTypes.BLOCK || node.elementType in COMMENTS
        }
    }

    override fun indexStub(stub: PsiFileStub<*>, sink: IndexSink) {
        super.indexStub(stub, sink)
        if (stub !is TactFileStub) return

        val fqn = stub.getModuleQualifiedName()
        if (fqn.isNotEmpty()) {
            sink.occurrence(TactModulesIndex.KEY, fqn)
        }

        val name = stub.getModuleName()
        if (name.isNotEmpty()) {
            sink.occurrence(TactModulesFingerprintIndex.KEY, name)
        }
    }

    override fun serialize(stub: TactFileStub, dataStream: StubOutputStream) {}

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): TactFileStub {
        return TactFileStub(null)
    }

//    Uncomment to find out what causes switch to the AST
//    private val PARESED = com.intellij.util.containers.ContainerUtil.newConcurrentSet<String>()
//    override fun doParseContents(chameleon: ASTNode, psi: com.intellij.psi.PsiElement): ASTNode? {
//        val path = psi.containingFile?.virtualFile?.path
//        if (path != null && PARESED.add(path)) {
//            println("Parsing (${PARESED.size}) $path")
//            val trace = java.io.StringWriter().also { writer ->
//                Exception().printStackTrace(java.io.PrintWriter(writer))
//                writer.toString()
//            }.toString()
//            if (!trace.contains("PsiSearchHelperImpl.lambda\$processPsiFileRoots\$8") &&
//                !trace.contains("TactFoldingBuilder.buildFoldRegion") &&
//                !trace.contains("TactTypeReferenceExpressionImpl.getIdentifier")
//            ) {
//                println(trace)
//                println()
//            }
//        }
//        return super.doParseContents(chameleon, psi)
//    }

    override fun getExternalId() = "tact.FILE"

    companion object {
        val INSTANCE = TactFileElementType()
        const val VERSION = 4
    }
}

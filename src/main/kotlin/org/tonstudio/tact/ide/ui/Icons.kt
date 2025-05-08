package org.tonstudio.tact.ide.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

object Icons {
    val Tact = IconLoader.getIcon("/icons/tact.svg", this::class.java)
    val Boc = IconLoader.getIcon("/icons/boc.svg", this::class.java)

    val Test = AllIcons.RunConfigurations.TestState.Run
    val TestGreen = AllIcons.RunConfigurations.TestState.Green2
    val TestRed = AllIcons.RunConfigurations.TestState.Red2

    val Struct = AllIcons.Nodes.Class
    val Trait = AllIcons.Nodes.Test // TODO: change
    val Contract = AllIcons.Nodes.Class // TODO: change
    val Primitive = AllIcons.Nodes.Parameter // TODO: change
    val Message = AllIcons.Nodes.Method // TODO: change
    val Method = AllIcons.Nodes.Method
    val Function = AllIcons.Nodes.Function
    val Variable = AllIcons.Nodes.Variable
    val Constant = AllIcons.Nodes.Constant
    val Parameter = AllIcons.Nodes.Parameter
    val Field = AllIcons.Nodes.Field
    val Directory = AllIcons.Nodes.Folder

    val SourceRoot = AllIcons.Modules.SourceRoot
    val LocalModulesRoot = AllIcons.Nodes.ModuleGroup
}

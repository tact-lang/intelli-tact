# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2025-05-05

Initial development release.

### Features

- feat(actions): create new Tact file action
- feat(build): add run configuration for project and single contract build
- feat(completion): add `Fill required fields...` and `Fill all fields...` completion, refactored struct instance completely
- feat(completion): add completion for TL-B types after `as` for fields
- feat(completion): add completion for assembly instructions
- feat(completion): add completion for contract init()
- feat(completion): add completion for functions and constants in contract/trait
- feat(completion): add completion for initOf and codeOf
- feat(completion): add completion for receiver, external and bounced receivers
- feat(completion): add completion for return
- feat(completion): add completion for top-level function definitions
- feat(completion): add getter completion
- feat(completion): add override completion
- feat(completion): add snippets
- feat(completion): initial completion support: reference completion, keyword completion, fields completion
- feat(documentation): add documentation for TL-B types
- feat(documentation): add documentation for traits and contracts
- feat(documentation): add hover documentation for asm instructions
- feat(documentation): add hover documentation for messages, structs, primitives and constants
- feat(documentation): add hover documentation for variables, parameters and fields
- feat(documentation): better highlighting for TL-B types
- feat(documentation): better support for attributes
- feat(documentation): initial documentation support for functions
- feat(editor): add gutter icon for recursive calls
- feat(editor): add structure view
- feat(editor): initial support for auto /// when hits enter inside doc comment
- feat(editor): support breadcrumbs and sticky lines
- feat(editor): support find usages and spell-checker for strings
- feat(editor): support signature help for functions calls
- feat(fmt): integrate Tact formatter
- feat(folding): add folding for asm function body and instruction sequences
- feat(highlighting): better highlighting for assembly
- feat(highlighting): highlight builtin functions like ton() in constant documentation
- feat(highlighting): highlight exit points when cursor on `fun` or `receive/external/bounced/init`
- feat(highlighting): highlight mutable variables with own underlined style
- feat(inlay-hints): add inlay hints for `require()` exit code
- feat(inlay-hints): add inlay hints for assembly instructions
- feat(inlay-hints): don't show parameter hints for unary functions from stubs like `ton()`
- feat(inlay-hints): don't show variable type in obvious cases
- feat(inlay-hints): show VCS author as an inlay hint
- feat(inlay-hints): show `as int257` hint for Int typed fields without a TL-B type
- feat(inlay-hints): show gas consumption for an assembly sequence
- feat(inlay-hints): show getter ids as inlay hints
- feat(inlay-hints): show type hint for catch variable
- feat(inspections): add inspection for unresolved symbols with quickfix to import a file with declaration
- feat(inspections): add inspections for unused and duplicate imports
- feat(navigation): add go to for class-like and all symbols
- feat(navigation): add navigation to parent constant definition and vice versa
- feat(resolving): add proper resolving and completion for import paths
- feat(resolving): support assembly shuffle resolving
- feat(resolving): support pseudo static methods `fromCell/fromSlice/opcode`
- feat(resolving): take into account imported files when resolve
- feat(resolving/inlay-hints): support resolving, type inference, and type hints for destruct bindings
- feat(search): implement searches for methods: `implementation -> super` and vice versa, add gutter icon with navigation to super/override methods, implement "Go to super" action
- feat(toolchain): add toolchain settings (#9)

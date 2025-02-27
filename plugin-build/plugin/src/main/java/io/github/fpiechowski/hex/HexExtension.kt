package io.github.fpiechowski.hex

import org.gradle.api.Project
import javax.inject.Inject

@Suppress("UnnecessaryAbstractClass", "EmptyClassBlock")
abstract class HexExtension
    @Inject
    constructor(
        @Suppress("UNUSED_PARAMETER") project: Project,
    )

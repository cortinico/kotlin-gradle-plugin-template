package io.github.fpiechowski.hex

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.kotlin.dsl.DependencyHandlerScope
import javax.inject.Inject

const val DEFAULT_PLANTUML_FILE = "hex.puml"

@Suppress("UnnecessaryAbstractClass")
abstract class HexExtension @Inject constructor(project: Project) {

    private val objects = project.objects

/*
    // Example of a property that is mandatory. The task will
    // fail if this property is not set as is annotated with @Optional.
    val message: Property<String> = objects.property(String::class.java)

    // Example of a property that is optional.
    val tag: Property<String> = objects.property(String::class.java)
*/

    // Example of a property with a default set with .convention
    val plantUmlFile: RegularFileProperty = objects.fileProperty().convention(
        project.layout.buildDirectory.file(DEFAULT_PLANTUML_FILE)
    )
}

fun DependencyHandlerScope.adapterImplementation(dependencyNotation: Any) {
    add("adapterImplementation", "$dependencyNotation")
}

fun DependencyHandlerScope.portImplementation(dependencyNotation: Any) {
    add("portImplementation", "$dependencyNotation")
}

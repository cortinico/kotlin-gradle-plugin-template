package io.github.fpiechowski.hex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

const val EXTENSION_NAME = "hex"
const val TASK_NAME = "generatePlantUML"

abstract class HexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Add the 'template' extension object
        val extension = project.extensions.create(EXTENSION_NAME, HexExtension::class.java, project)

        with(project.extensions.getByType(JavaPluginExtension::class.java)) {

            sourceSets.run {
                val port = create("port")

                val adapter = create("adapter") {
                    it.runtimeClasspath += port.output
                    it.compileClasspath += port.output
                }

                getByName("main") {
                    it.runtimeClasspath += port.output
                    it.compileClasspath += port.output

                    it.runtimeClasspath += adapter.output
                    it.compileClasspath += adapter.output
                }

                getByName("test") {
                    it.runtimeClasspath += port.output
                    it.compileClasspath += port.output

                    it.runtimeClasspath += adapter.output
                    it.compileClasspath += adapter.output
                }
            }

            project.configurations.run {
                val portImplementation = getByName("portImplementation")
                val adapterImplementation = getByName("adapterImplementation") {
                    it.extendsFrom(portImplementation)
                }

                getByName("implementation") {
                    it.extendsFrom(portImplementation)
                    it.extendsFrom(adapterImplementation)
                }

                getByName("testImplementation") {
                    it.extendsFrom(portImplementation)
                    it.extendsFrom(adapterImplementation)
                }
            }
        }

        // Add a task that uses configuration from the extension object
        project.tasks.register(TASK_NAME, GeneratePlantUML::class.java) {
            it.outputFile.set(extension.plantUmlFile)
        }
    }
}

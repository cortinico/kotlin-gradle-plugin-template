package io.github.fpiechowski.hex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

const val EXTENSION_NAME = "hex"

abstract class HexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, HexExtension::class.java, project)

        val portName = extension.portName.get()
        val domainName = extension.domainName.get()
        val adapterName = extension.adapterName.get()

        with(project.extensions.getByType(JavaPluginExtension::class.java)) {
            setupSourceSets(portName, domainName, adapterName, project)
            setupConfigurations(project, portName, domainName, adapterName)
        }
    }

    private fun JavaPluginExtension.setupSourceSets(
        portName: String,
        domainName: String,
        adapterName: String,
        project: Project
    ) {
        sourceSets.run {
            val port = create(portName) {
                it.java.srcDir("src/$domainName/java")
                if (project.plugins.hasPlugin("kotlin")) {
                    it.java.srcDir("src/$domainName/kotlin")
                }
            }

            val domain = create(domainName) {
                it.runtimeClasspath += port.output
                it.compileClasspath += port.output
            }

            val adapter = create(adapterName) {
                it.runtimeClasspath += domain.output
                it.compileClasspath += domain.output

                it.runtimeClasspath += port.output
                it.compileClasspath += port.output
            }

            getByName("main") {
                it.runtimeClasspath += port.output
                it.compileClasspath += port.output

                it.runtimeClasspath += domain.output
                it.compileClasspath += domain.output

                it.runtimeClasspath += adapter.output
                it.compileClasspath += adapter.output
            }

            getByName("test") {
                it.runtimeClasspath += port.output
                it.compileClasspath += port.output

                it.runtimeClasspath += domain.output
                it.compileClasspath += domain.output

                it.runtimeClasspath += adapter.output
                it.compileClasspath += adapter.output
            }
        }
    }

    private fun setupConfigurations(
        project: Project,
        portName: String,
        domainName: String,
        adapterName: String
    ) {
        project.configurations.run {
            val portImplementation = getByName("${portName}Implementation")

            val domainImplementation = getByName("${domainName}Implementation") {
                it.extendsFrom(portImplementation)
            }

            val adapterImplementation = getByName("${adapterName}Implementation") {
                it.extendsFrom(domainImplementation)
                it.extendsFrom(portImplementation)
            }

            getByName("implementation") {
                it.extendsFrom(domainImplementation)
                it.extendsFrom(portImplementation)
                it.extendsFrom(adapterImplementation)
            }

            getByName("testImplementation") {
                it.extendsFrom(domainImplementation)
                it.extendsFrom(portImplementation)
                it.extendsFrom(adapterImplementation)
            }
        }
    }
}

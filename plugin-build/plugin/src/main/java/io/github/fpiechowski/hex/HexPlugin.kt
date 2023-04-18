package io.github.fpiechowski.hex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

abstract class HexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            val extension = project.extensions.create("hex", HexExtension::class.java, project)

            val portName = extension.portName.get()
            val domainName = extension.domainName.get()
            val adapterName = extension.adapterName.get()

            setupSourceSets(portName, domainName, adapterName)
            setupConfigurations(domainName, adapterName)
        }
    }

    private fun Project.setupSourceSets(portName: String, domainName: String, adapterName: String) {
        with(project.extensions.getByType(JavaPluginExtension::class.java)) {
            sourceSets.run {
                val domain = create(domainName) {
                    it.java.srcDir(listOf("src/$domainName/$portName/java"))

                    if (plugins.hasPlugin("kotlin")) {
                        it.java.srcDirs("src/$domainName/$portName/kotlin")
                    }
                }

                val adapter = create(adapterName) {
                    it.runtimeClasspath += domain.output
                    it.compileClasspath += domain.output
                }

                getByName("main") {
                    it.runtimeClasspath += domain.output
                    it.compileClasspath += domain.output

                    it.runtimeClasspath += adapter.output
                    it.compileClasspath += adapter.output
                }

                getByName("test") {
                    it.runtimeClasspath += domain.output
                    it.compileClasspath += domain.output

                    it.runtimeClasspath += adapter.output
                    it.compileClasspath += adapter.output
                }
            }
        }
    }

    private fun Project.setupConfigurations(domainName: String, adapterName: String) {
        configurations.run {
            val implementation = getByName("implementation")

            val domainImplementation = getByName("${domainName}Implementation") {
                it.extendsFrom(implementation)
            }

            getByName("${adapterName}Implementation") {
                it.extendsFrom(implementation)
                it.extendsFrom(domainImplementation)
            }
        }
    }
}

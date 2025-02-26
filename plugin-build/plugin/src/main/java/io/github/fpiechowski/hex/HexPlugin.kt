package io.github.fpiechowski.hex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

abstract class HexPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.run {
            val extension = project.extensions.create("hex", HexExtension::class.java, project)

            setupSourceSets()
            setupConfigurations()
            setupJars()
        }
    }

    private fun Project.setupJars() {
        with(extensions.getByType(JavaPluginExtension::class.java)) {
            tasks.named<Jar>("jar") {
                from(sourceSets[DOMAIN].output)
            }

            tasks.register<Jar>("${DOMAIN}Jar") {
                from(sourceSets[DOMAIN].output)
                archiveBaseName.set("${project.name}-domain")
                group = "build"
            }
        }
    }

    private fun Project.setupSourceSets() {
        with(extensions.getByType(JavaPluginExtension::class.java)) {
            sourceSets.run {
                val domain = create(DOMAIN)

                val domainRuntimeClasspath = configurations.getByName("${DOMAIN}RuntimeClasspath")
                val domainCompileClasspath = configurations.getByName("${DOMAIN}CompileClasspath")

                getByName("main") {
                    it.runtimeClasspath += domainRuntimeClasspath + domain.output
                    it.compileClasspath += domainCompileClasspath + domain.output
                }

                getByName("test") {
                    it.runtimeClasspath += domainRuntimeClasspath + domain.output
                    it.compileClasspath += domainCompileClasspath + domain.output
                }
            }
        }
    }

    private fun Project.setupConfigurations() {
        configurations.run {
            val domainImplementation = getByName("${DOMAIN}Implementation") {
                it.isCanBeResolved = true
                it.isCanBeConsumed = true
            }

            getByName("implementation") {
                it.extendsFrom(domainImplementation)
            }
        }
    }

    companion object {
        const val DOMAIN = "domain"
    }
}

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

            val portName = extension.portName.get()
            val domainName = extension.domainName.get()
            val adapterName = extension.adapterName.get()

            setupSourceSets(portName, domainName, adapterName)
            setupConfigurations(domainName, adapterName)
            setupJars(domainName, adapterName)

            artifacts {
                it.add("${domainName}Implementation", tasks.named("domainJar"))
                it.add("${adapterName}Implementation", tasks.named("adapterJar"))
            }
        }
    }

    private fun Project.setupJars(domainName: String, adapterName: String) {
        with(extensions.getByType(JavaPluginExtension::class.java)) {
            tasks.named<Jar>("jar") {
                from(sourceSets[domainName].output)
            }

            val domainJar = tasks.register<Jar>("domainJar") {
                from(sourceSets[domainName].output)
                archiveClassifier.set("domain")
            }

            val adapterJar = tasks.register<Jar>("adapterJar") {
                from(sourceSets[adapterName].output)
                archiveClassifier.set("adapter")
            }

            tasks.named("assemble") {
                it.dependsOn(domainJar, adapterJar)
            }
        }
    }

    private fun Project.setupSourceSets(portName: String, domainName: String, adapterName: String) {
        with(extensions.getByType(JavaPluginExtension::class.java)) {
            sourceSets.run {
                val domain = create(domainName) {
                    it.java.srcDir(listOf("src/$domainName/$portName/java"))

                    if (plugins.hasPlugin("kotlin")) {
                        it.java.srcDirs("src/$domainName/$portName/kotlin")
                    }
                }

                val domainRuntimeClasspath = configurations.getByName("${domainName}RuntimeClasspath")
                val domainCompileClasspath = configurations.getByName("${domainName}CompileClasspath")

                val adapter = create(adapterName) {
                    it.runtimeClasspath += domainRuntimeClasspath + domain.output
                    it.compileClasspath += domainCompileClasspath + domain.output
                }

                val adapterRuntimeClasspath = configurations.getByName("${adapterName}RuntimeClasspath")
                val adapterCompileClasspath = configurations.getByName("${adapterName}CompileClasspath")

                getByName("main") {
                    it.runtimeClasspath += domainRuntimeClasspath + domain.output
                    it.compileClasspath += domainCompileClasspath + domain.output

                    it.runtimeClasspath += adapterRuntimeClasspath + adapter.output
                    it.compileClasspath += adapterCompileClasspath + adapter.output
                }

                getByName("test") {
                    it.runtimeClasspath += domainRuntimeClasspath + domain.output
                    it.compileClasspath += domainCompileClasspath + domain.output

                    it.runtimeClasspath += adapterRuntimeClasspath + adapter.output
                    it.compileClasspath += adapterCompileClasspath + adapter.output
                }
            }
        }
    }

    private fun Project.setupConfigurations(domainName: String, adapterName: String) {
        configurations.run {
            val domainImplementation = getByName("${domainName}Implementation") {
                it.isCanBeResolved = true
                it.isCanBeConsumed = true
            }

            val adapterImplementation = getByName("${adapterName}Implementation") {
                it.extendsFrom(domainImplementation)
                it.isCanBeResolved = true
                it.isCanBeConsumed = true
            }

            getByName("implementation") {
                it.extendsFrom(domainImplementation)
                it.extendsFrom(adapterImplementation)
            }
        }
    }
}

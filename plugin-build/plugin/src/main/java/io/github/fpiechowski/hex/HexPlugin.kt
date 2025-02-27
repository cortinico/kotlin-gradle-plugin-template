package io.github.fpiechowski.hex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

abstract class HexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            @Suppress("UNUSED_VARIABLE")
            val extension = project.extensions.create("hex", HexExtension::class.java, project)

            setupSourceSets()
            setupJars()

            plugins.withType<MavenPublishPlugin> {
                setupPublishing()
            }
        }
    }

    private fun Project.setupPublishing() {
        extensions.configure<PublishingExtension> {
            publications.apply {
                create<MavenPublication>("domain") {
                    artifactId = "${project.name}-domain"
                    artifact(tasks["domainJar"])
                }
            }
        }
    }

    private fun Project.setupJars() {
        with(extensions.getByType(JavaPluginExtension::class.java)) {
            tasks.named<Jar>("jar") {
                from(sourceSets["domain"].output)
            }

            tasks.register<Jar>("domainJar") {
                archiveBaseName.set("${project.name}-domain")
                from(sourceSets["domain"].output)
                group = "build"
            }

            tasks.named<Jar>("shadowJar") {
                from(sourceSets["domain"].output)
            }
        }
    }

    private fun Project.setupSourceSets() {
        with(extensions.getByType(JavaPluginExtension::class.java)) {
            sourceSets.run {
                val domain = create(DOMAIN)

                getByName("main").apply {
                    compileClasspath += domain.output
                    runtimeClasspath += domain.output
                }
            }
        }
    }

    companion object {
        const val DOMAIN = "domain"
    }
}

package io.github.fpiechowski.hex

import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertNotNull
import org.junit.Test

class HexPluginTest {

    @Test
    fun `extension hexConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("java")
        project.pluginManager.apply("io.github.fpiechowski.hex.plugin")

        assertNotNull(project.extensions.getByName("hex"))
    }

    @Test
    fun `sourceSets are created`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("java")
        project.pluginManager.apply("io.github.fpiechowski.hex.plugin")

        val extension = project.extensions.getByType<HexExtension>()

        with(project.extensions.getByType<JavaPluginExtension>()) {
            val domain = sourceSets.getByName(extension.domainName.get()).apply {
                java.srcDirs.any { it.path.endsWith("/src/domain/java") }
                java.srcDirs.any { it.path.endsWith("/src/domain/port/java") }
            }
            val adapter = sourceSets.getByName(extension.adapterName.get()).apply {
                assert(runtimeClasspath.files.containsAll(domain.runtimeClasspath.files))
                assert(compileClasspath.files.containsAll(domain.compileClasspath.files))
            }

            listOf(sourceSets.getByName("main"), sourceSets.getByName("test")).forEach {
                assert(
                    it.runtimeClasspath.files.containsAll(
                        domain.runtimeClasspath.files + adapter.runtimeClasspath.files
                    )
                )
                assert(
                    it.compileClasspath.files.containsAll(
                        domain.compileClasspath.files + adapter.compileClasspath.files
                    )
                )
            }
        }
    }
}

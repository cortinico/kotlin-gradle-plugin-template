package io.github.fpiechowski.hex

import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File

class HexPluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.fpiechowski.hex.plugin")

        assert(project.tasks.getByName("generatePlantUML") is GeneratePlantUML)
    }

    @Test
    fun `extension hexConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.fpiechowski.hex.plugin")

        assertNotNull(project.extensions.getByName("hex"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.fpiechowski.hex.plugin")
        val aFile = File(project.projectDir, ".tmp")
        (project.extensions.getByName("hex") as HexExtension).apply {
            /*tag.set("a-sample-tag")
            message.set("just-a-message")*/
            plantUmlFile.set(aFile)
        }

        val task = project.tasks.getByName("generatePlantUML") as GeneratePlantUML

        /*assertEquals("a-sample-tag", task.tag.get())
        assertEquals("just-a-message", task.message.get())*/
        assertEquals(aFile, task.outputFile.get().asFile)
    }

    @Test
    fun `sourceSets are created`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("io.github.fpiechowski.hex.plugin")

        with(project.extensions.getByType<JavaPluginExtension>()) {
            val port = sourceSets.getByName("port")
            val adapter = sourceSets.getByName("adapter").apply {
                assert(runtimeClasspath.files.containsAll(port.runtimeClasspath.files))
                assert(compileClasspath.files.containsAll(port.compileClasspath.files))
            }

            listOf(sourceSets.getByName("main"), sourceSets.getByName("test")).forEach {
                assert(
                    it.runtimeClasspath.files.containsAll(
                        port.runtimeClasspath.files + adapter.runtimeClasspath.files
                    )
                )
                assert(
                    it.compileClasspath.files.containsAll(
                        port.compileClasspath.files + adapter.compileClasspath.files
                    )
                )
            }
        }
    }
}

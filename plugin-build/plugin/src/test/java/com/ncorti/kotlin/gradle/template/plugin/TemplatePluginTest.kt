package com.ncorti.kotlin.gradle.template.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class TemplatePluginTest {
    @JvmField
    @Rule
    var testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")

        assert(project.tasks.getByName("templateExample") is TemplateExampleTask)
    }

    @Test
    fun `extension templateExampleConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")

        assertNotNull(project.extensions.getByName("templateExampleConfig"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")
        val aFile = File(project.projectDir, ".tmp")
        (project.extensions.getByName("templateExampleConfig") as TemplateExtension).apply {
            tag.set("a-sample-tag")
            message.set("just-a-message")
            outputFile.set(aFile)
        }

        val task = project.tasks.getByName("templateExample") as TemplateExampleTask

        assertEquals("a-sample-tag", task.tag.get())
        assertEquals("just-a-message", task.message.get())
        assertEquals(aFile, task.outputFile.get().asFile)
    }

    @Test
    fun `task generates file with message`() {
        val message = "Just trying this gradle plugin..."
        testProjectDir.root.removeRecursively()
        File(testProjectDir.root, "build.gradle")
            .writeText(
                generateBuildFile("message.set(\"$message\")\ntag.set(\"tag\")"),
            )

        val gradleResult = executeGradleRun("templateExample")
        assert(gradleResult.output.contains("message is: $message"))

        val generatedFileText = (testProjectDir.root / "build" / "template-example.txt").readText()
        assert(generatedFileText == "[tag] $message")
    }

    private fun executeGradleRun(task: String): BuildResult =
        GradleRunner
            .create()
            .withProjectDir(testProjectDir.root)
            .withArguments(task)
            .withPluginClasspath()
            .build()

    private fun generateBuildFile(config: String) =
        """
        plugins {
            id 'com.ncorti.kotlin.gradle.template.plugin'
        }
        templateExampleConfig {
            $config
        }
        """.trimIndent()
}

private fun File.removeRecursively() =
    this
        .walkBottomUp()
        .filter { it != this }
        .forEach { it.deleteRecursively() }

private operator fun File.div(s: String): File = this.resolve(s)

package io.github.fpiechowski.hex

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GeneratePlantUML : DefaultTask() {

    init {
        description = "Generate a PlantUML diagram from annotated ports and adapters in code"

        // Don't forget to set the group here.
        // group = BasePlugin.BUILD_GROUP
    }

    /*
        @get:Input
        @get:Option(option = "message", description = "A message to be printed in the output file")
        abstract val message: Property<String>

        @get:Input
        @get:Option(option = "tag", description = "A Tag to be used for debug and in the output file")
        @get:Optional
        abstract val tag: Property<String>
    */

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun sampleAction() {
        outputFile.get().asFile.writeText(
            """
            @startuml
            left to right direction

            rectangle interfaces #lightgreen {
                interface http

                component http_adapter
            }

            hexagon domain #lightyellow;line:red;line.bold {
                hexagon  {
                }
                component service

                portin control
                portout persistence
            }

            rectangle infrastructure #orange {
                database database
                component database_adapter
            }

            http -> http_adapter
            http_adapter -(0-> control
            control -> service
            service -> persistence
            persistence -(0-> database_adapter
            database_adapter -> database

            @enduml
            """.trimIndent()
        )
    }
}

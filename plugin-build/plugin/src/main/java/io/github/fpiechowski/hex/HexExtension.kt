package io.github.fpiechowski.hex

import org.gradle.api.Project
import org.gradle.api.provider.Property
import javax.inject.Inject

@Suppress("UnnecessaryAbstractClass")
abstract class HexExtension @Inject constructor(project: Project) {
    val domainName: Property<String> = project.objects.property(String::class.java).convention("domain")
    val portName: Property<String> = project.objects.property(String::class.java).convention("port")
    val adapterName: Property<String> = project.objects.property(String::class.java).convention("adapter")
}

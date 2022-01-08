pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = ("kotlin-gradle-plugin-template")

include(":example")
includeBuild("plugin-build")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = ("kotlin-gradle-plugin-template")

include(":example")
includeBuild("plugin-build")

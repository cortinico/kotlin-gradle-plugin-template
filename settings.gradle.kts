pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.gradle.develocity") version "3.19.2"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
        publishing {
            onlyIf { System.getenv("GITHUB_ACTIONS") == "true" }
        }
    }
}

rootProject.name = "hex-gradle-plugin"

includeBuild("plugin-build")
include(":example:dependency", ":example")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
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

rootProject.name = ("io.github.fpiechowski.hex")

include(":plugin")

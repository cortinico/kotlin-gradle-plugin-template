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
	id("com.gradle.develocity") version "3.18.2"
}

develocity {
	buildScan.termsOfUseUrl = "https://gradle.com/terms-of-service"
	buildScan.termsOfUseAgree = "yes"
	buildScan.publishing.onlyIf {
		System.getenv("GITHUB_ACTIONS") == "true" &&
				it.buildResult.failures.isNotEmpty()
	}
}

rootProject.name = ("com.ncorti.kotlin.gradle.template")

include(":plugin")

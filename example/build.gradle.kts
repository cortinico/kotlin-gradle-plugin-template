plugins {
    kotlin("jvm") version "1.8.20"
    id("io.github.fpiechowski.hex.plugin")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("nested/build/lib")
    }
}

dependencies {
    domainImplementation(":nested-domain:0.1")
}

tasks.named("compileDomainKotlin") {
    dependsOn(":example:nested:jar")
}

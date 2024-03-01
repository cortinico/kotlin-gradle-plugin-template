plugins {
    kotlin("jvm") version "1.8.20"
    id("io.github.fpiechowski.hex.plugin")
}

dependencies {
    // implementation(project("nested"))
    domainImplementation(project("nested"))
    adapterImplementation(project("nested"))
    adapterImplementation(project("nested", configuration = "adapterImplementation")) // depending on adapter is not recommended, do it only if you wish to extend the adapter
}

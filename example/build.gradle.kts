plugins {
    kotlin("jvm") version "1.8.20"
    id("io.github.fpiechowski.hex.plugin")
}

hex {
    portName.set("port")
    domainName.set("domain")
    adapterName.set("adapter")
}

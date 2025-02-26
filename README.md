# hex-gradle-plugin

### Hexagonal Architecture Plugin

## How to use

```kotlin
plugins {
    kotlin("jvm") version "<<verison>>"
    id("io.github.fpiechowski.hex.plugin")
}
```

This will create an isolated source set `domain`.
All symbols from `domain` are accessible in `main` by default, but not the other way around.
This protects domain code from being dependent on adapter code.

## Domain dependencies
If you want to add dependencies to the `domain` source set you need to do it via `domainImplementation` dependency configuration.

```kotlin
dependencies {
    domainImplementation("group:artifact:version")
}
```

## Domain artifact
You can use `domainJar` to create artifact containing sources from just the `domain` source set.
That allows you to modularize your domain sources.

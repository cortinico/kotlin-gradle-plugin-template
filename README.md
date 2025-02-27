# hex-gradle-plugin

### Hexagonal Architecture Plugin

## How to use

```kotlin
plugins {
    id("io.github.fpiechowski.hex")
}
```

This will create an isolated source set `domain`.
All symbols from `domain` are accessible in `main` and `test` by default, but not the other way around.
This protects domain code from being dependent on adapter code.

It will also configure default `jar` Gradle task as well as `shadowJar` task to include `domain` source set.


## Domain dependencies
If you want to add dependencies to the `domain` source set you need to do it via `domain<<configuration>>` dependency configurations, eg. `domainImplementation`.

```kotlin
dependencies {
    domainImplementation("group:artifact:version")
}
```

## Domain artifact and publication
You can use `domainJar` to create artifact containing sources from just the `domain` source set.
That allows you to modularize your domain sources.

Additionally, the plugin prepares `domain` publication for `maven-publish` to publish domain-only artifact.

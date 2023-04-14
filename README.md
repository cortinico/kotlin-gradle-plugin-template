# hex-gradle-plugin

### Hexagonal Architecture Plugin

## How to use

```kotlin
plugins {
    kotlin("jvm") version "1.8.20"
    id("io.github.fpiechowski.hex.plugin")
}

hex {
    portName.set("port") // optional
    domainName.set("domain") // optional
    adapterName.set("adapter") // optional
}

```


## Composite Build 📦

This plguin is using a [Gradle composite build](https://docs.gradle.org/current/userguide/composite_builds.html) to build, test and publish the plugin. This means that you don't need to run Gradle twice to test the changes on your Gradle plugin (no more `publishToMavenLocal` tricks or so).

The included build is inside the [plugin-build](plugin-build) folder.

### `preMerge` task

A `preMerge` task on the top level build is already provided in the plugin. This allows you to run all the `check` tasks both in the top level and in the included build.

You can easily invoke it with:

```
./gradlew preMerge
```

If you need to invoke a task inside the included build with:

```
./gradlew -p plugin-build <task-name>
```


## Publishing

This plugin is ready to let you publish to [Gradle Portal](https://plugins.gradle.org/).

The [![Publish Plugin to Portal](https://github.com/cortinico/hex-gradle-plugin/workflows/Publish%20Plugin%20to%20Portal/badge.svg?branch=1.0.0)](https://github.com/cortinico/hex-gradle-plugin/actions?query=workflow%3A%22Publish+Plugin+to+Portal%22) Github Action will take care of the publishing whenever you **push a tag**.

## 100% Kotlin

This plugin is designed to use Kotlin everywhere. The build files are written using [**Gradle Kotlin DSL**](https://docs.gradle.org/current/userguide/kotlin_dsl.html) as well as the [Plugin DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block) to setup the build.

Dependencies are centralized inside the [libs.versions.toml](gradle/libs.versions.toml).

Moreover, a minimalistic Gradle Plugin is already provided in Kotlin to let you easily start developing your own around it.

## Static Analysis

This plugin is using [**ktlint**](https://github.com/pinterest/ktlint) with the [ktlint-gradle](https://github.com/jlleitschuh/ktlint-gradle) plugin to format your code. To reformat all the source code as well as the buildscript you can run the `ktlintFormat` gradle task.

This plugin is also using [**detekt**](https://github.com/arturbosch/detekt) to analyze the source code, with the configuration that is stored in the [detekt.yml](config/detekt/detekt.yml) file (the file has been generated with the `detektGenerateConfig` task).

## CI

This plugin is using [**GitHub Actions**](https://github.com/cortinico/kotlin-android-plugin/actions) as CI. You don't need to setup any external service and you should have a running CI once you start using this plugin.

There are currently the following workflows available:
- [Validate Gradle Wrapper](.github/workflows/gradle-wrapper-validation.yml) - Will check that the gradle wrapper has a valid checksum
- [Pre Merge Checks](.github/workflows/pre-merge.yaml) - Will run the `preMerge` tasks as well as trying to run the Gradle plugin.
- [Publish to Plugin Portal](.github/workflows/publish-plugin.yaml) - Will run the `publishPlugin` task when pushing a new tag.

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.

## License

This plugin is licensed under the MIT License.

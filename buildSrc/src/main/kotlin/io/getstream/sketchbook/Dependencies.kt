package io.getstream.sketchbook

object Versions {
    internal const val ANDROID_GRADLE_PLUGIN = "7.3.0"
    internal const val ANDROID_GRADLE_SPOTLESS = "6.3.0"
    internal const val GRADLE_NEXUS_PUBLISH_PLUGIN = "1.1.0"
    internal const val KOTLIN = "1.7.20"
    internal const val KOTLIN_GRADLE_DOKKA = "1.7.20"
    internal const val KOTLIN_BINARY_VALIDATOR = "0.11.1"
    internal const val KOTLIN_COROUTINE = "1.6.4"

    internal const val MATERIAL = "1.6.1"
    internal const val ANDROIDX_CORE_KTX = "1.5.0"

    internal const val COMPOSE = "1.3.0-rc01"
    internal const val COMPOSE_COMPILER = "1.3.2"
    internal const val COMPOSE_ACTIVITY = "1.4.0"
    internal const val COMPOSE_MATERIAL3 = "1.0.0-alpha02"
    internal const val COLOR_PICKER = "1.2.0"
    internal const val PHOTO_PICKER = "1.0.0-alpha06"

    internal const val ANDROIDX_TEST_VERSION = "1.4.0"
    internal const val BASE_PROFILE_VERSION = "1.2.0"
    internal const val MACRO_BENCHMARK_VERSION = "1.2.0-alpha01"
    internal const val ANDROIDX_UI_AUTOMATOR_VERSION = "2.2.0"
}

object Dependencies {
    const val androidGradlePlugin =
        "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val gradleNexusPublishPlugin =
        "io.github.gradle-nexus:publish-plugin:${Versions.GRADLE_NEXUS_PUBLISH_PLUGIN}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val spotlessGradlePlugin =
        "com.diffplug.spotless:spotless-plugin-gradle:${Versions.ANDROID_GRADLE_SPOTLESS}"
    const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.KOTLIN_GRADLE_DOKKA}"
    const val kotlinBinaryValidator =
        "org.jetbrains.kotlinx:binary-compatibility-validator:${Versions.KOTLIN_BINARY_VALIDATOR}"
    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINE}"

    const val material = "com.google.android.material:material:${Versions.MATERIAL}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE_KTX}"

    const val composeUI = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.COMPOSE}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.COMPOSE}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.COMPOSE}"
    const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
    const val composeMaterial3 =
        "androidx.compose.material3:material3:${Versions.COMPOSE_MATERIAL3}"
    const val colorPicker = "com.github.skydoves:orchestra-colorpicker:${Versions.COLOR_PICKER}"
    const val photoPicker =
        "com.google.modernstorage:modernstorage-photopicker:${Versions.PHOTO_PICKER}"

    const val BASE_PROFILE =
        "androidx.profileinstaller:profileinstaller:${Versions.BASE_PROFILE_VERSION}"
    const val MACRO_BENCHMARK =
        "androidx.benchmark:benchmark-macro-junit4:${Versions.MACRO_BENCHMARK_VERSION}"
    const val ANDROIDX_UI_AUTOMATOR =
        "androidx.test.uiautomator:uiautomator:${Versions.ANDROIDX_UI_AUTOMATOR_VERSION}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST_VERSION}"
}

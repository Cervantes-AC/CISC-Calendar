// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Compose compiler plugin is no longer applied via alias at top-level
    // Each module that uses Compose applies it individually:
    // id("org.jetbrains.kotlin.android.compose")

    // Google services Gradle plugin for Firebase
    id("com.google.gms.google-services") version "4.4.3" apply false
}

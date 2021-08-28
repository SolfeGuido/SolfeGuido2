val kotlinVersion: String by project

buildscript {
    val androidPluginVersion: String by project
    val kotlinVersion: String by project
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$androidPluginVersion")
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

plugins {
    base
}
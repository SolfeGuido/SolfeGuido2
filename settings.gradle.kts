dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        google()
        gradlePluginPortal()
    }
}

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("plugin.serialization") version(kotlinVersion)
    }
}


include("core", "lwjgl3", "android")
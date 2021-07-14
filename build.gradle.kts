
plugins {
    kotlin("jvm") version Versions.KOTLIN
}


subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    val implementation by configurations

    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.contracts.ExperimentalContracts"
        }
    }
}
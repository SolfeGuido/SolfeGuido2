plugins {
    kotlin("jvm") version "1.5.20" apply false
    kotlin("plugin.serialization") version "1.5.20"
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
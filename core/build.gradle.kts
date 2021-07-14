plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.KOTLIN
}

dependencies {
    val kotlin = Versions.KOTLIN
    val coroutines = Versions.KOTLIN_COROUTINES
    val gdx = Versions.GDX
    val ktx = Versions.KTX
    val jsonSerialization = Versions.KOTLIN_SERIALIZATION
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    api("com.badlogicgames.gdx:gdx:$gdx")
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlin")
    api("com.badlogicgames.gdx:gdx-freetype:$gdx")
    api("io.github.libktx:ktx-collections:$ktx")
    api("io.github.libktx:ktx-assets:$ktx")
    api("io.github.libktx:ktx-async:$ktx")
    api("io.github.libktx:ktx-assets-async:$ktx")
    api("io.github.libktx:ktx-actors:$ktx")
    api("io.github.libktx:ktx-log:$ktx")
    api("io.github.libktx:ktx-i18n:$ktx")
    api("io.github.libktx:ktx-freetype:$ktx")
    api("io.github.libktx:ktx-freetype-async:$ktx")
    api("io.github.libktx:ktx-scene2d:$ktx")
    api("io.github.libktx:ktx-app:$ktx")
    api("io.github.libktx:ktx-math:$ktx")
    api("io.github.libktx:ktx-inject:$ktx")
    api("io.github.libktx:ktx-style:$ktx")
    api("io.github.libktx:ktx-graphics:$ktx")
    api("io.github.libktx:ktx-preferences:$ktx")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$jsonSerialization")
}
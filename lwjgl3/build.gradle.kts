plugins {
    application
    kotlin("jvm")
}


val gdxVersion: String by project
val mainClassName = "io.github.solfeguido2.lwjgl3.DesktopLauncherKt"
val assetsDir = rootProject.file("assets")
val appVersion : String by project
val applicationName : String by project

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("io.github.solfeguido2.lwjgl3.DesktopLauncherKt")

    version = appVersion
    applicationName = applicationName
}

sourceSets {
    main {
        resources.srcDir(rootProject.file("assets"))
    }
}


dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
}


// Use this task to create a fat jar.
tasks.register<Jar>("dist") {
    from(files(sourceSets.main.get().output.classesDirs))
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    from(assetsDir)

    manifest {
        duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.INCLUDE
        attributes["Main-Class"] = mainClassName
    }
}

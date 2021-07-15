plugins {
    application
}

application {
    mainClass.set("io.github.solfeguido.lwjgl3.DesktopLauncherKt")

    version = Versions.SOLFEGUIDO
    applicationName = Versions.SOLFEGUIDO_VERION
}

sourceSets {
    main {
        resources.srcDir(rootProject.file("assets"))
    }
}

dependencies {
    val gdx = Versions.GDX
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdx")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdx:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdx:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdx:natives-desktop")
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        dependsOn(configurations.runtimeClasspath)
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

        archiveFileName.set("${application.applicationName}-${project.version}.jar")

        manifest {
            attributes["Main-Class"] = application.mainClass.get()
        }
    }
}
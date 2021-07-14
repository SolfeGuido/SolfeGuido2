plugins {
    application
}

application {
    mainClass.set("io.github.solfeguido.lwjgl3.DesktopLauncherKt")

    version = "2.0.0-SNAPSHOT"
    applicationName = "SolfeGuido"
}

sourceSets {
    main {
        resources.srcDir(rootProject.file("assets"))
    }
}

dependencies {
    val gdx = project.property("gdxVersion")
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
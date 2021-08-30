plugins {
    id("com.android.application")
    kotlin("android")
}


android {

    buildToolsVersion("30.0.3")
    compileSdkVersion(30)
    sourceSets {
        named("main") {
            res.srcDir("res")
            assets.srcDir("../assets/")
            jniLibs.srcDir("libs")
            manifest.srcFile("AndroidManifest.xml")
        }

    }
    defaultConfig {
        val appVersion: String by project
        applicationId = "io.github.solfeguido2"
        minSdkVersion(14)
        targetSdkVersion(30)
        versionCode = appVersion.split('.').joinToString("") { it.padStart(2, '0') }.toInt()
        versionName = appVersion
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lintOptions {
        isAbortOnError = false
    }
}

val natives: Configuration by configurations.creating

dependencies {
    val gdxVersion: String by project

    implementation(project(":core"))

    implementation(kotlin("stdlib"))

    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")


    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")

    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
}




// Called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
tasks.register("copyAndroidNatives") {
    doFirst {
        natives.files.forEach { jar ->
            val outputDir = file("libs/" + jar.nameWithoutExtension.substringAfterLast("natives-"))
            outputDir.mkdirs()
            copy {
                from(zipTree(jar))
                into(outputDir)
                include("*.so")
            }
        }
    }
}

tasks.whenTaskAdded {
    if ("package" in name) {
        dependsOn("copyAndroidNatives")
    }
}

// 
// tasks {
// 
//     val run by registering(Exec::class) {
//         val localProperties = project.file("../local.properties")
//         val path = if (localProperties.exists()) {
//             val properties = Properties()
//             properties.load(localProperties.inputStream())
//             val sdkDir = properties.getProperty("sdk.dir")
//             if (sdkDir.isNullOrEmpty()) {
//                 System.getenv("ANDROID_HOME")
//             } else {
//                 sdkDir
//             }
//         } else {
//             System.getenv("ANDROID_HOME")
//         }
// 
//         val adb = path + "/platform-tools/adb"
//         commandLine =
//             listOf("$adb", "shell", "am", "start", "-n", "io.github.solfeguido2/io.github.solfeguido2.AndroidLauncher")
//     }
// 
// 
// }
// 
android {
    buildToolsVersion "29.0.3"
    compileSdkVersion 29
    sourceSets {
        main {
            manifest.srcFile 'AndroidManifest.xml'
            java.srcDirs = ['src']
            aidl.srcDirs = ['src']
            renderscript.srcDirs = ['src']
            res.srcDirs = ['res']
            assets.srcDirs = ['../assets/']
            jniLibs.srcDirs = ['libs']
        }

    }
    packagingOptions {
        exclude 'META-INF/robovm/ios/robovm.xml'
    }
    defaultConfig {
        applicationId "io.github.solfeguido2"
        minSdkVersion 14
        targetSdkVersion 29
        versionCode 1
        versionName "2.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}


// called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
task copyAndroidNatives {
    doFirst {
        file("libs/armeabi/").mkdirs()
        file("libs/armeabi-v7a/").mkdirs()
        file("libs/arm64-v8a/").mkdirs()
        file("libs/x86_64/").mkdirs()
        file("libs/x86/").mkdirs()

        configurations.natives.files.each { jar ->
            def outputDir = null
            if (jar.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
            if (jar.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
            if(jar.name.endsWith("natives-armeabi.jar")) outputDir = file("libs/armeabi")
            if(jar.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
            if(jar.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
            if(outputDir != null) {
                copy {
                    from zipTree(jar)
                    into outputDir
                    include "*.so"
                }
            }
        }
    }
}

tasks.whenTaskAdded { packageTask ->
    if (packageTask.name.contains("package")) {
        packageTask.dependsOn 'copyAndroidNatives'
    }
}

task run(type: Exec) {
    def path
    def localProperties = project.file("../local.properties")
    if (localProperties.exists()) {
        Properties properties = new Properties()
        localProperties.withInputStream { instr ->
            properties.load(instr)
        }
        def sdkDir = properties.getProperty('sdk.dir')
        if (sdkDir) {
            path = sdkDir
        } else {
            path = "$System.env.ANDROID_HOME"
        }
    } else {
        path = "$System.env.ANDROID_HOME"
    }

    def adb = path + "/platform-tools/adb"
    commandLine "$adb", 'shell', 'am', 'start', '-n', 'io.github.solfeguido2/io.github.solfeguido2.AndroidLauncher'
}

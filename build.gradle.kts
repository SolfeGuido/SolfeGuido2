import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

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
tasks.register("nls") {
    val project = "core"
    val source = "src/main/kotlin"
    val packagename = "io.github.solfeguido2.enums"
    val name = "Nls"
    val filename = "nls.kt"
    val bundle = "assets/i18n/nls.properties"

    val builder = StringBuilder(
        """package $packagename
        
import ktx.i18n.BundleLine
import com.badlogic.gdx.utils.I18NBundle
/** Generated from $bundle file. */
enum class $name(private val key: String) : BundleLine {

    """.trimIndent()
    )

    val newLine = System.getProperty("line.separator")
    file(bundle).forEachLine {
        val data = it.trim()
        val separatorIndex = data.indexOf("=")
        if (data.isNotBlank() && separatorIndex > 0 && !data.startsWith("#")) {
            val keyId = data.substring(0, separatorIndex).trim()
            val keyName = keyId
                .split(".", "_")
                .map { it.capitalizeAsciiOnly() }
                .joinToString("")
            builder.append("    ").append(keyName).append("(\"").append(keyId).append("\"),").append(newLine)
        }
    }

    builder.append(
        """;
    override fun toString() = key

    override val bundle: I18NBundle
        get() = i18nBundle

    companion object {
        lateinit var i18nBundle: I18NBundle
    }
}
    """.trimIndent()
    )

    val actualSource = source.replace("/", File.separator)
    val actualPackage = packagename.replace(".", File.separator)
    val targetPath = listOf(project, actualSource, actualPackage, filename).joinToString(File.separator)
    println("Saving i18n bundle enum at ${targetPath}...")
    val enumFile = file(targetPath)
    enumFile.delete()
    enumFile.parentFile.mkdirs()
    enumFile.createNewFile()
    enumFile.writeText(builder.append(newLine).toString())
    println("Done. I18n bundle enum generated.")
}

plugins {
    base
}
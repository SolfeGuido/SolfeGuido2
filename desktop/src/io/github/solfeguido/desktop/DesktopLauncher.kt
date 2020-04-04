package io.github.solfeguido.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.github.solfeguido.SolfeGuido

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "SolfeGuido"
        config.width = 1280
        config.height = 720
        config.resizable = false
        LwjglApplication(SolfeGuido(), config)
    }
}
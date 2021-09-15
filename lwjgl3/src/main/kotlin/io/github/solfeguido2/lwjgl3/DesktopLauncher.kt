package io.github.solfeguido2.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import io.github.solfeguido2.SolfeGuido

fun main(arg: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("SolfeGuido")
    config.setWindowSizeLimits(1280, 720, 1280, 720)
    Lwjgl3Application(SolfeGuido(), config)
}
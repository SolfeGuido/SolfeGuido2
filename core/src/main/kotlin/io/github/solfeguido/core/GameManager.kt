package io.github.solfeguido.core

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.solfeguido.factories.firePooled
import io.github.solfeguido.settings.GameSettings
import io.github.solfeguido.structures.GameStats
import ktx.inject.Context

class GameManager(private val context: Context, val settings: GameSettings) {

    private val stats = GameStats()
    private val events = context.inject<Actor>()
    var startTime = 0L


    fun star() {
        startTime = System.currentTimeMillis()
    }

    fun end() {
        if(startTime > 0L) {
            stats.timePlayed = ((System.currentTimeMillis() - startTime) / 1000f).toInt()
        }

    }

}
package io.github.solfeguido.events

import com.badlogic.gdx.scenes.scene2d.Event
import io.github.solfeguido.structures.GameStats

class GameFinishedEvent(var stats: GameStats = GameStats()) : Event() {

    override fun reset() {
        super.reset()
        stats = GameStats()
    }
}


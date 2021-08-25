package io.github.solfeguido2.events

import com.badlogic.gdx.scenes.scene2d.Event
import io.github.solfeguido2.structures.GameStats

class GameFinishedEvent(var stats: GameStats = GameStats()) : Event() {

    override fun reset() {
        super.reset()
        stats = GameStats()
    }
}


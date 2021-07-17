package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.structures.progression.Level
import io.github.solfeguido.core.LevelManager
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.RandomGenerator
import kotlinx.serialization.Serializable
import ktx.inject.Context

@Serializable
class LevelOptions(
    val level: Level
    ) : NoteGuessOptions(
    listOf(
        MeasureSettings(
            level.clef,
            generator = RandomGenerator(level.requirements.lowerNote, level.requirements.higherNote)
        )
    ), true
) {


    override fun endGame(context: Context, score: Int) {
        actors.forEach { it.terminate() }
        //TODO: add special effect is the level is unlocked (and use real score)
        context.inject<LevelManager>().registerLevelScore(level.clef, 0, score)
    }
}
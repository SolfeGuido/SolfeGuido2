package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.core.progression.Level
import io.github.solfeguido.core.progression.LevelManager
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.RandomGenerator
import ktx.collections.gdxArrayOf
import ktx.inject.Context

class LevelOptions(val level: Level) : NoteGuessOptions(
    gdxArrayOf(
        MeasureSettings(level.clef)
    //TODO change generator
    ), RandomGenerator(level.requirements.lowerNote, level.requirements.higherNote), true
) {

    override fun endGame(context: Context, score: Int) {
        actors.forEach { it.terminate() }
        //TODO: add special effect is the level is unlocked (and use real score)
        context.inject<LevelManager>().registerLevelScore(level.clef, 0, score)
    }
}
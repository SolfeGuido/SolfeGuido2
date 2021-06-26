package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.core.LevelManager
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.RandomGenerator
import ktx.collections.gdxArrayOf
import ktx.inject.Context

class LevelOptions(
    val clef: ClefEnum,
    val level: Int,
) : NoteGuessOptions(
    gdxArrayOf(
        MeasureSettings(clef)
    ), RandomGenerator(), true
) {

    override fun endGame(context: Context, score: Int) {
        actors.forEach { it.terminate() }
        //TODO: add special effect is the level is unlocked
        context.inject<LevelManager>().passedLevel(clef, level, score)
    }
}
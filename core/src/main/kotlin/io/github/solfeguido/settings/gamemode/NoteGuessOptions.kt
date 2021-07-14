package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.core.StatsManager
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.factories.measure
import io.github.solfeguido.factories.onResult
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.ui.events.ResultEvent
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.collections.toGdxArray
import ktx.inject.Context
import ktx.scene2d.KStack

@Serializable
open class NoteGuessOptions(
    var measures: List<MeasureSettings> = listOf(),
    var isCustom: Boolean = true
) : IGameModeOptions {

    @Transient
    var currentMeasure = 0

    @Transient
    var actors: GdxArray<MeasureActor> = gdxArrayOf()


    override fun populateScene(context: Context, parent: KStack, resultCallback: (ResultEvent) -> Unit) {
        currentMeasure = 0
        actors = measures.map {
            parent.measure(it, context.inject<PreferencesManager>().noteStyle) {
                onResult { resultCallback(it) }
            }
        }.toGdxArray()
    }

    override fun endGame(context: Context, score: Int) {
        actors.forEach {
            it.terminate()
        }

        if (measures.size == 1 && !isCustom) {
            context.inject<StatsManager>().saveGameScore(measures[0].clef, score)
        }
    }

    override fun validateNote(note: NoteOrderEnum): Boolean {
        val measure = actors[currentMeasure]
        currentMeasure = (currentMeasure + 1) % actors.size
        return measure.checkNote(note)
    }

    override fun hasAccidentals(): Boolean {
        return measures.any { it.signature.symbol != NoteAccidentalEnum.Natural || it.generator.hasAccidentals() }
    }

}
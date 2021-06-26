package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.core.StatsManager
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.factories.measure
import io.github.solfeguido.factories.onResult
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.IGeneratorOptions
import io.github.solfeguido.settings.generator.RandomGenerator
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.collections.toGdxArray
import ktx.inject.Context
import ktx.json.readValue
import ktx.scene2d.KStack

open class NoteGuessOptions(
    var measures: GdxArray<MeasureSettings> = gdxArrayOf(),
    var generator: IGeneratorOptions = RandomGenerator(),
    var isCustom: Boolean = true
) : IGameModeOptions {

    var currentMeasure = 0
    var actors: GdxArray<MeasureActor> = gdxArrayOf()

    override fun read(json: Json, jsonData: JsonValue) {
        measures = json.readValue(jsonData, "measures")
        generator = IGeneratorOptions.toInstance(json, jsonData)
        isCustom = json.readValue(jsonData, "isCustom")
    }

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("measures", measures)
        json.writeValue("generator", generator)
        json.writeValue("isCustom", isCustom)
    }

    override fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {
        currentMeasure = 0
        actors = measures.map {
            parent.measure(it.clef) {
                onResult { resultCallback(it) }
            }
        }.toGdxArray()
    }

    override fun endGame(context: Context, score: Int) {
        actors.forEach {
            it.terminate()
        }

        if(measures.size == 1 && !isCustom) {
            context.inject<StatsManager>().saveGameScore(measures[0].clef, score)
        }
    }

    override fun validateNote(note: NoteOrderEnum): Boolean {
        val measure = actors[currentMeasure]
        currentMeasure = (currentMeasure + 1) % actors.size
        return measure.checkNote(note)
    }

}
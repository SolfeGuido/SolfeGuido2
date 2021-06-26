package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.gdxArrayOf
import ktx.inject.Context
import ktx.json.readValue
import ktx.scene2d.KStack

sealed interface IGameModeOptions : Json.Serializable {

    companion object {

        fun toInstance(json: Json, jsonData: JsonValue): IGameModeOptions {
            val type: String = json.readValue(jsonData, "className")
            return when (type) {
                NoteGuessOptions::class.java.simpleName -> NoteGuessOptions()
                EarTrainingOptions::class.java.simpleName -> EarTrainingOptions()
                IntervalGuessOptions::class.java.simpleName -> IntervalGuessOptions()
                KeySignatureGuessOptions::class.java.simpleName -> KeySignatureGuessOptions()
                else -> throw Exception("Invalid type '$type' of game options")
            }.also { it.read(json, jsonData) }
        }

        fun classicGame(clef: ClefEnum) = NoteGuessOptions(
            measures = gdxArrayOf(
                MeasureSettings(
                    signature = KeySignatureEnum.CMajor,
                    clef = clef,
                    canHaveAccidentals = false,
                    minNote = 60,
                    maxNote = 90,
                )
            ),
            isCustom = false
        )

        fun levelGame(clef: ClefEnum, level: Int) = LevelOptions(clef, level)

    }

    override fun read(json: Json, jsonData: JsonValue) {
    }

    override fun write(json: Json) {
        json.writeValue("className", this.javaClass.simpleName)
    }

    fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit)

    fun validateNote(note: NoteOrderEnum): Boolean

    fun endGame(context: Context, score: Int)

}
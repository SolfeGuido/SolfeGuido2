package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.scene2d.KStack
import ktx.json.*

sealed interface IGameModeOptions : Json.Serializable {

    companion object {

        fun toInstance(json: Json, jsonData: JsonValue) : IGameModeOptions {
            val type : String = json.readValue( jsonData, "className")
            return when(type) {
                NoteGuessOptions::class.java.simpleName -> NoteGuessOptions()
                EarTrainingOptions::class.java.simpleName -> EarTrainingOptions()
                IntervalGuessOptions::class.java.simpleName -> IntervalGuessOptions()
                KeySignatureGuessOptions::class.java.simpleName -> KeySignatureGuessOptions()
                else -> throw Exception("Invalid type '$type' of game options")
            }.also { it.read(json, jsonData) }
        }

    }

    override fun read(json: Json, jsonData: JsonValue) {}

    override fun write(json: Json) {

    }

    fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit)

    fun validateNote(note: NoteOrderEnum) : Boolean
}
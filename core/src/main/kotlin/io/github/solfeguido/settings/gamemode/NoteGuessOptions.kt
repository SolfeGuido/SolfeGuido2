package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.factories.measure
import io.github.solfeguido.factories.onResult
import io.github.solfeguido.settings.GeneratorSettings
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.ui.events.ResultEvent
import ktx.json.readValue
import ktx.scene2d.KStack

class NoteGuessOptions : IGameModeOptions {


    var keySignature = KeySignatureEnum.CMajor
    var measures: List<MeasureSettings> = emptyList()
    var generator: GeneratorSettings = GeneratorSettings()

    override fun read(json: Json, jsonData: JsonValue) {
        keySignature = json.readValue(jsonData, "keySignature")
        measures = json.readValue(jsonData, "measures")
        generator = json.readValue(jsonData, "generator")
    }

    override fun write(json: Json) {
        json.writeValue("keySignature", keySignature)
        json.writeValue("measures", measures)
        json.writeValue("generator", generator)
    }

    override fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {
        measures.forEach {
            parent.measure {
                onResult { resultCallback(it) }
            }
        }
    }

    override fun validateNote(note: NoteOrderEnum) = false
}
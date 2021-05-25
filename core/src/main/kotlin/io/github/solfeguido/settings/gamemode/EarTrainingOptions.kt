package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.settings.GeneratorSettings
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.ui.events.ResultEvent
import ktx.json.*
import ktx.scene2d.KStack

class EarTrainingOptions : IGameModeOptions {

    var keySignature = KeySignatureEnum.CMajor
    var measure: MeasureSettings = MeasureSettings()
    var generator: GeneratorSettings = GeneratorSettings()

    override fun read(json: Json, jsonData: JsonValue) {
        keySignature = json.readValue(jsonData, "keySignature")
        measure = json.readValue(jsonData, "measure")
        generator = json.readValue(jsonData, "generator")
    }

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("generator", generator)
        json.writeValue("measure", measure)
        json.writeValue("keySignature", keySignature)
    }

    override fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {
    // TODO
    }

    override fun endGame() {
        // TODO
    }

    override fun validateNote(note: NoteOrderEnum) = false
}
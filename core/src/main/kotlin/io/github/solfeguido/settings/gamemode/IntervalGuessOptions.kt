package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.json.*
import ktx.scene2d.KStack

class IntervalGuessOptions : IGameModeOptions {

    var minInterval = 1
    var maxInterval = 12
    var keySignature = KeySignatureEnum.CMajor

    override fun read(json: Json, jsonData: JsonValue) {
        minInterval = json.readValue(jsonData, "minInterval")
        maxInterval = json.readValue(jsonData, "maxInterval")
        keySignature = json.readValue(jsonData, "keySignature")
    }

    override fun write(json: Json) {
        json.writeValue("minInterval", minInterval)
        json.writeValue("maxInterval", maxInterval)
        json.writeValue("keySignature", keySignature)
    }

    override fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {

    }

    override fun validateNote(note: NoteOrderEnum) = false
}
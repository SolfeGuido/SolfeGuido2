package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.gdxArrayOf
import ktx.json.*
import ktx.scene2d.KStack

class KeySignatureGuessOptions : IGameModeOptions {

    var signatures: Array<KeySignatureEnum> = gdxArrayOf()

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("signatures", signatures)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        signatures = json.readValue(jsonData, "signatures")
    }

    override fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {

    }

    override fun endGame() {
        // TODO
    }

    override fun validateNote(note: NoteOrderEnum) = false

}
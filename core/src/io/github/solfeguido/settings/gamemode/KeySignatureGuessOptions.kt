package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import ktx.json.*

class KeySignatureGuessOptions : IGameModeOptions {

    var signatures: List<KeySignatureEnum> = emptyList()

    override fun write(json: Json) {
        json.writeValue("signatures", signatures)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        signatures = json.readValue(jsonData, "signatures")
    }

}
package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.readValue

class GameSettings : Json.Serializable {

    var gameModeSettings = GameModeSettings()

    override fun write(json: Json) {
        json.writeValue("gameMode", gameModeSettings)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        gameModeSettings = json.readValue(jsonData, "gameMode")
    }

}
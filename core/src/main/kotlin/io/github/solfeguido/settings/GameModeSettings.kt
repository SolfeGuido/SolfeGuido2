package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.settings.gamemode.*
import ktx.json.*

class GameModeSettings : Json.Serializable {

    var time = TimeSettings()
    var options: IGameModeOptions = NoteGuessOptions()

    override fun write(json: Json) {
        json.writeValue("time", time)
        json.writeValue("options", options)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        time = json.readValue(jsonData, "time")
        options = IGameModeOptions.toInstance(json, jsonData)
    }
}
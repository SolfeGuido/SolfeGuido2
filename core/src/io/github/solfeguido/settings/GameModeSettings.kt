package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.GameModeEnum
import io.github.solfeguido.settings.gamemode.*
import ktx.json.*

class GameModeSettings : Json.Serializable {

    var type = GameModeEnum.Note
    var time = TimeSettings()
    var options: IGameModeOptions = NoteGuessOptions()

    override fun write(json: Json) {
        json.writeValue("type", type)
        json.writeValue("time", time)
        json.writeValue("options", options)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        time = json.readValue(jsonData, "time")
        type = json.readValue(jsonData, "type")
        options = when(type) {
            GameModeEnum.Note -> json.readValue<NoteGuessOptions>(jsonData, "options")
            GameModeEnum.EarTraining -> json.readValue<EarTrainingOptions>(jsonData, "options")
            GameModeEnum.Interval -> json.readValue<IntervalGuessOptions>(jsonData, "options")
            GameModeEnum.KeySignature -> json.readValue<KeySignatureGuessOptions>(jsonData, "options")
        }
    }
}
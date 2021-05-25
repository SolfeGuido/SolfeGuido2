package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.settings.gamemode.IGameModeOptions
import io.github.solfeguido.settings.gamemode.NoteGuessOptions
import ktx.collections.gdxArrayOf
import ktx.json.readValue

class GameSettings(
    var time: TimeSettings = TimeSettings(),
    var options: IGameModeOptions = NoteGuessOptions()
) : Json.Serializable {

    companion object {

        fun classicWithTimer(clef: ClefEnum, time: TimeSettings) = GameSettings(
            time = time,
            options = IGameModeOptions.classicGame(clef)
        )

    }

    override fun write(json: Json) {
        json.writeValue("time", time)
        json.writeValue("options", options)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        time = json.readValue(jsonData, "time")
        options = IGameModeOptions.toInstance(json, jsonData)
    }
}
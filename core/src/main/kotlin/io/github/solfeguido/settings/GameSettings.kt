package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.core.progression.Level
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.settings.gamemode.IGameModeOptions
import io.github.solfeguido.settings.gamemode.LevelOptions
import io.github.solfeguido.settings.gamemode.NoteGuessOptions
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

        fun levelGame(level: Level) = GameSettings(
            time = TimeSettings.ClassicCountdownMode,
            options = LevelOptions(level)
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
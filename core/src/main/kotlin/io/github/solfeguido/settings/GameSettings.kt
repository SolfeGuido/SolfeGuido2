package io.github.solfeguido.settings

import io.github.solfeguido.structures.progression.Level
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.settings.gamemode.IGameModeOptions
import io.github.solfeguido.settings.gamemode.LevelOptions
import io.github.solfeguido.settings.gamemode.NoteGuessOptions
import kotlinx.serialization.Serializable

@Serializable
class GameSettings(
    var time: TimeSettings = TimeSettings(),
    var options: IGameModeOptions = NoteGuessOptions()
)  {

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
}
package io.github.solfeguido2.settings

import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.structures.progression.Level
import io.github.solfeguido2.settings.gamemode.IGameModeOptions
import io.github.solfeguido2.settings.gamemode.LevelOptions
import io.github.solfeguido2.settings.gamemode.NoteGuessOptions
import kotlinx.serialization.Serializable

@Serializable
class GameSettings(
    var time: TimeSettings = TimeSettings(),
    var options: IGameModeOptions = NoteGuessOptions()
) {

    companion object {

        fun levelGame(level: Level) = GameSettings(
            time = TimeSettings.ClassicCountdownMode,
            options = LevelOptions(level)
        )

        fun autoGame(clef: ClefEnum) = GameSettings(
            time = TimeSettings.InfiniteAutoMode,
            options = IGameModeOptions.classicGame(clef)
        )

    }
}
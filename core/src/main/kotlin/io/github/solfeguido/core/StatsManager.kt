package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.events.ResultEvent
import io.github.solfeguido.settings.GameSettings
import io.github.solfeguido.structures.GameSave
import io.github.solfeguido.structures.GameStats
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ktx.preferences.get
import ktx.preferences.set
import ktx.log.error

class StatsManager(private val preference: Preferences, private val serializer: Json) {

    private var wrongNotes = hashMapOf<NoteOrderEnum, Int>()
    private var saves = mutableListOf<GameSave>()

    fun allSaves() = saves.toList()

    fun registerResult(event: ResultEvent) {
        if (!event.isCorrect) {
            wrongNotes[event.expected] = (wrongNotes[event.expected] ?: 0) + 1
        }
    }

    fun saveGameScore(settings: GameSettings, score: GameStats) {
        saves.add(GameSave(settings, score))
    }

    fun save() {
        preference[Constants.Preferences.WRONGE_NOTES] = serializer.encodeToString(wrongNotes)
        preference[Constants.Preferences.GAME_SAVES] = serializer.encodeToString(saves)
        preference.flush()
    }

    fun loadSave() {
        try {
            val gameSaves: String? = preference[Constants.Preferences.GAME_SAVES]
            if (!gameSaves.isNullOrBlank()) {
                saves = serializer.decodeFromString(gameSaves)
            }
        } catch (ex: Exception) {
            error(ex) { "Failed to load game saves" }
        }

        try {
            val wrongNotesData: String? = preference[Constants.Preferences.WRONGE_NOTES]
            if (!wrongNotesData.isNullOrBlank()) {
                wrongNotes = serializer.decodeFromString(wrongNotesData)
            }

        } catch (ex : Exception) {
            error(ex) { "Failed to load note stats" }
        }

    }
}
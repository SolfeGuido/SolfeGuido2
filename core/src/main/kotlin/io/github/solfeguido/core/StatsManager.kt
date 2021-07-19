package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.events.ResultEvent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ktx.preferences.get
import ktx.preferences.set

class StatsManager(private val preference: Preferences) {

    private var wrongNotes = hashMapOf<NoteOrderEnum, Int>()
    private var bestScores = hashMapOf<ClefEnum, Int>()

    fun registerResult(event: ResultEvent) {
        if (!event.isCorrect) {
            wrongNotes[event.expected] = (wrongNotes[event.expected] ?: 0) + 1
        }
    }

    fun saveGameScore(clef: ClefEnum, score: Int) {
        val best = bestScores[clef] ?: 0
        if (best < score) {
            bestScores[clef] = score
        }
    }

    fun save() {
        preference[Constants.Preferences.WRONGE_NOTES] = Json.encodeToString(wrongNotes)
        preference[Constants.Preferences.BEST_SCORES] = Json.encodeToString(bestScores)
        preference.flush()
    }

    fun loadSave() {
        val bestScoresData: String? = preference[Constants.Preferences.BEST_SCORES]
        if (!bestScoresData.isNullOrBlank()) {
            bestScores = Json.decodeFromString(bestScoresData)
        }
        val wrongNotesData: String? = preference[Constants.Preferences.WRONGE_NOTES]
        if (!wrongNotesData.isNullOrBlank()) {
            wrongNotes = Json.decodeFromString(wrongNotesData)
        }


    }
}
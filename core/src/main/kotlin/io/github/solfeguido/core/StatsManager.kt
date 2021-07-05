package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.gdxMapOf
import ktx.collections.set
import ktx.json.fromJson
import ktx.preferences.set

class StatsManager(private val preference: Preferences) {

    private var wrongNotes = gdxMapOf<NoteOrderEnum, Int>()
    private var bestScores = gdxMapOf<ClefEnum, Int>()

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
        val json = Json()
        val wrongNotesSerialized = json.toJson(wrongNotes)
        preference["WRONG_NOTES"] = wrongNotesSerialized

        val scoresSerialized = json.toJson(bestScores)
        preference["BEST_SCORES"] = scoresSerialized

        preference.flush()
    }

    fun loadSave() {
        val json = Json()
        wrongNotes = json.fromJson(preference.getString("WRONG_NOTES"))
        bestScores = json.fromJson(preference.getString("BEST_SCORES"))

    }
}
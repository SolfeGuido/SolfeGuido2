package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.GdxMap
import ktx.collections.gdxMapOf
import ktx.collections.set
import ktx.json.fromJson
import ktx.preferences.get
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
        Json().apply {
            preference[Constants.Preferences.STATS] = toJson(
                gdxMapOf(
                    "WRONG_NOTES" to wrongNotes,
                    "BEST_SCORES" to bestScores
                )
            )
            preference.flush()
        }

    }

    fun loadSave() {
        val json = Json()
        val originalStr: String? = preference[Constants.Preferences.STATS]

        if (!originalStr.isNullOrBlank()) {
            val globObj: GdxMap<String, JsonValue> = json.fromJson(originalStr)
            val keys = globObj.get("WRONG_NOTES")
//            for(entry in keys) {
//                println(entry)
//
//            }

            //wrongNotes = json.fromJson(globObj["WRONG_NOTES"])
            //bestScores = Json().fromJson(Json().toJson(globObj.get("BEST_SCORES")))
        }


    }
}
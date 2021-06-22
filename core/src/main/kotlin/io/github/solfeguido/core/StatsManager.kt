package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.gdxMapOf
import ktx.collections.set
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
        for(entry in wrongNotes) {
            preference["WRONGS_${entry.key.name}"] = entry.value
        }
        for(score in bestScores) {
            preference["BEST_SCORE_${score.key.name}"] = score.value
        }
        preference.flush()
    }

    fun loadSave() {
        for (n in NoteOrderEnum.values()) {
            val name = "WRONGS_${n.name}"
            preference.getInteger(name)?.let { wrongNotes[n] = it }
        }

        for (clef in ClefEnum.values()) {
            val name = "BEST_SCORE_${clef.name}"
            preference.getInteger(name)?.let { bestScores[clef] = it }
        }
    }
}
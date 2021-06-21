package io.github.solfeguido.core

import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.gdxMapOf
import ktx.collections.set

class StatsManager {

    private var wrongNotes = gdxMapOf<NoteOrderEnum, Int>()

    fun registerResult(event: ResultEvent) {
        if(!event.isCorrect) {
            wrongNotes[event.expected] = (wrongNotes[event.expected] ?: 0) + 1
        }
    }

    fun saveGameScore() {
        //TODO
    }

    fun flush() {
        //TODO
    }

    fun loadSave() {
        //TODO
    }
}
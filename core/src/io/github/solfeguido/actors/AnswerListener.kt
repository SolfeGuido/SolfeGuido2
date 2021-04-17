package io.github.solfeguido.actors

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputListener
import io.github.solfeguido.enums.NoteOrderEnum

class AnswerGivenEvent(val note: NoteOrderEnum) : Event()

abstract class AnswerListener : InputListener() {

    override fun handle(e: Event?): Boolean {
        if(e !is AnswerGivenEvent) return false
        answerGiven(e.note)
        return false
    }

    abstract fun answerGiven(note: NoteOrderEnum)
}
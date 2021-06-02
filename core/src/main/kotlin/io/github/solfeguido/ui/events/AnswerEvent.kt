package io.github.solfeguido.ui.events

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import io.github.solfeguido.enums.NoteOrderEnum

class AnswerGivenEvent(var note: NoteOrderEnum = NoteOrderEnum.C) : Event() {

    override fun reset() {
        super.reset()
        note = NoteOrderEnum.C
    }
}

class AnswerListener(private val handler: (event: AnswerGivenEvent) -> Boolean) : EventListener {

    override fun handle(e: Event?) = if (e is AnswerGivenEvent) handler(e) else false

}
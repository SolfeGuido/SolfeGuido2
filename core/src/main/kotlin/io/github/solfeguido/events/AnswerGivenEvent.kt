package io.github.solfeguido.events

import com.badlogic.gdx.scenes.scene2d.Event
import io.github.solfeguido.enums.NoteOrderEnum


class AnswerGivenEvent(var note: NoteOrderEnum = NoteOrderEnum.C) : Event() {

    override fun reset() {
        super.reset()
        note = NoteOrderEnum.C

    }
}
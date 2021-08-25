package io.github.solfeguido2.events

import com.badlogic.gdx.scenes.scene2d.Event
import io.github.solfeguido2.enums.NoteOrderEnum


class AnswerGivenEvent(var note: NoteOrderEnum = NoteOrderEnum.C) : Event() {

    override fun reset() {
        super.reset()
        note = NoteOrderEnum.C

    }
}
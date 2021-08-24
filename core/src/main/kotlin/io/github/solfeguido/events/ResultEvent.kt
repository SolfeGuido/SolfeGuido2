package io.github.solfeguido.events

import com.badlogic.gdx.scenes.scene2d.Event
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.structures.MidiNote

class ResultEvent(
    var expected: MidiNote = MidiNote.DEFAULT_NOTE,
    var actual: NoteOrderEnum = NoteOrderEnum.C
) : Event() {

    val isCorrect get() = expected.noteOrder == actual

    override fun reset() {
        super.reset()
        expected = MidiNote.DEFAULT_NOTE
        actual = NoteOrderEnum.C
    }
}

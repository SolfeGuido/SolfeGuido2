package io.github.solfeguido.events

import com.badlogic.gdx.scenes.scene2d.Event
import io.github.solfeguido.enums.NoteOrderEnum

class ResultEvent(
    var expected: NoteOrderEnum = NoteOrderEnum.C,
    var actual: NoteOrderEnum = NoteOrderEnum.C
) : Event() {

    val isCorrect get() = expected == actual

    override fun reset() {
        super.reset()
        expected = NoteOrderEnum.C
        actual = NoteOrderEnum.C
    }
}

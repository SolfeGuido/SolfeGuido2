package io.github.solfeguido.ui.events

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
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

class ResultListener(private val handler: (event: ResultEvent) -> Boolean) : EventListener {
    override fun handle(event: Event?) = if (event is ResultEvent) handler(event) else false
}
package io.github.solfeguido.ui.events

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import io.github.solfeguido.enums.NoteOrderEnum

class ResultEvent : Event() {

    var expected = NoteOrderEnum.C
    var actual = NoteOrderEnum.C

    val isCorrect get() = expected == actual
}

class ResultListener(private val handler: (event: ResultEvent) -> Boolean) : EventListener {
    override fun handle(event: Event?) = if (event is ResultEvent) handler(event) else false
}
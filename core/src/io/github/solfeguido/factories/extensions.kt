package io.github.solfeguido.factories

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.solfeguido.actors.AnswerListener
import io.github.solfeguido.enums.NoteOrderEnum


fun Actor.inside(x: Float, y : Float) = x in 0f..width && y in 0f..height

inline fun <T: Actor> T.onAnswer(crossinline listener: T.(note: NoteOrderEnum) -> Unit): AnswerListener {
    val answerListener = object: AnswerListener() {
        override fun answerGiven(note: NoteOrderEnum) {
            listener(note)
        }
    }
    addListener(answerListener)
    return answerListener
}
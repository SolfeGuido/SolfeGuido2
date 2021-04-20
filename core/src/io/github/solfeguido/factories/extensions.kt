package io.github.solfeguido.factories


import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.solfeguido.ui.events.AnswerGivenEvent
import io.github.solfeguido.ui.events.AnswerListener
import io.github.solfeguido.ui.events.ResultEvent
import io.github.solfeguido.ui.events.ResultListener


fun Actor.inside(x: Float, y: Float) = x in 0f..width && y in 0f..height

inline fun <T : Actor> T.onAnswer(crossinline listener: T.(event: AnswerGivenEvent) -> Unit): AnswerListener {
    val answerListener = AnswerListener { ev ->
        listener(ev)
        true
    }
    addListener(answerListener)
    return answerListener
}


inline fun <T : Actor> T.onResult(crossinline listener : T.(event: ResultEvent) -> Unit): ResultListener {
    val resultListener = ResultListener { ev ->
        listener(ev)
        true
    }
    addListener(resultListener)
    return resultListener
}
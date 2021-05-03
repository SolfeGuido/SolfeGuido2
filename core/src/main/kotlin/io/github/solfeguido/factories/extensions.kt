package io.github.solfeguido.factories


import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import io.github.solfeguido.ui.events.*


fun Actor.inside(x: Float, y: Float) = x in 0f..width && y in 0f..height

inline fun <T : Actor> T.onAnswer(crossinline listener: T.(event: AnswerGivenEvent) -> Unit) =
    AnswerListener { ev ->
        listener(ev)
        true
    }.also {
        addListener(it)

    }


inline fun <T : Actor> T.onResult(crossinline listener: T.(event: ResultEvent) -> Unit) =
    ResultListener { ev ->
        listener(ev)
        true
    }.also {
        addListener(it)

    }

inline fun <T : Actor> T.onTimerEnd(crossinline listener: T.(event: TimerEvent) -> Unit) =
    TimerListener { ev ->
        listener(ev)
        true
    }.also {
        addListener(it)
    }
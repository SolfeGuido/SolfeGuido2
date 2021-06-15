package io.github.solfeguido.factories


import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Pools
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

inline fun <T : Actor> T.onDialogHide(crossinline listener: T.(event: DialogHideEvent) -> Unit) =
    DialogHideListener { ev ->
        listener(ev)
        true
    }.also {
        addListener(it)
    }

inline fun <reified T, R> withPooled(crossinline consumer: (T) -> R): R {
    val obtained = Pools.obtain(T::class.java)
    try {
        return consumer.invoke(obtained)
    } finally {
        Pools.free(obtained)
    }

}
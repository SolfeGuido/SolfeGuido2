package io.github.solfeguido2.factories


import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.Pools
import io.github.solfeguido2.events.*


fun Actor.inside(x: Float, y: Float) = x in 0f..width && y in 0f..height


inline fun <reified E : Event> createLister(crossinline handler: (event: E) -> Boolean) =
    EventListener { event -> if (event is E) handler(event) else false }

inline fun <T : Actor, reified E : Event> T.registerListener(crossinline handler: T.(event: E) -> Boolean) =
    createLister<E> { event -> handler(event) }.also { listener -> addListener(listener) }

inline fun <T : Actor> T.onGameFinish(crossinline listener: T.(event: GameFinishedEvent) -> Boolean) =
    registerListener(listener)

inline fun <T : Actor> T.onLayoutChange(crossinline listener: T.(event: LayoutEvent) -> Boolean) =
    registerListener(listener)

inline fun <T : Actor> T.onAnswer(crossinline listener: T.(event: AnswerGivenEvent) -> Boolean) =
    registerListener(listener)


inline fun <T : Actor> T.onResult(crossinline listener: T.(event: ResultEvent) -> Boolean) = registerListener(listener)

inline fun <T : Actor> T.onTimerEnd(crossinline listener: T.(event: TimerFinishedEvent) -> Boolean) =
    registerListener(listener)

inline fun <T : Actor> T.onDialogHide(crossinline listener: T.(event: DialogHideEvent) -> Boolean) =
    registerListener(listener)

inline fun <T : Actor> T.onGiveAnswer(crossinline listener: T.(event: GiveAnswerEvent) -> Boolean) =
    registerListener(listener)

inline fun <reified T, R : Any> withPooled(crossinline consumer: (T) -> R): R {
    val obtained = Pools.obtain(T::class.java)
    try {
        return consumer.invoke(obtained)
    } finally {
        Pools.free(obtained)
    }
}

inline fun <reified T : Event> Actor.firePooled(crossinline applier: T.() -> Unit = {}) {
    withPooled<T, Unit> {
        applier.invoke(it)
        this.fire(it)
    }
}
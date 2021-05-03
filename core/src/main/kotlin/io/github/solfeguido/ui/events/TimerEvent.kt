package io.github.solfeguido.ui.events

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener

class TimerEvent : Event() {

}

class TimerListener(private val handler: (event: TimerEvent) -> Boolean) : EventListener {

    override fun handle(event: Event?): Boolean = if(event is TimerEvent) handler(event) else false
}
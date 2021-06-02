package io.github.solfeguido.ui.events

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener

class DialogHideEvent : Event()

class DialogHideListener(private val handler: (event: DialogHideEvent) -> Boolean) : EventListener {
    override fun handle(event: Event?) = if (event is DialogHideEvent) handler(event) else false
}




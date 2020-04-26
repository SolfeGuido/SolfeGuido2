package io.github.solfeguido.ui

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.borderButton
import io.github.solfeguido.factories.borderContainer
import io.github.solfeguido.factories.borderLabel
import io.github.solfeguido.factories.gCol
import ktx.actors.onClick
import ktx.scene2d.*

class AnswerButton(note: String): Stack(), KGroup {

    init {
        isTransform = true

        container {
            borderButton(note) {
                onClick { print("inside button clicked") }
            }
        }.fill().pad(10f)

        val icon = IconName.SharpAccidental.value
        val parent = Container<BorderContainer<Label>>()
        val child = BorderContainer<Label>()
        val label = Label(icon, Scene2DSkin.defaultSkin, "iconStyle")
        child.actor = label
        label.color = gCol("font")
        child.pad(5f)
        parent.actor = child
        parent.top().right().padTop(5f)
        add(parent)
    }
}
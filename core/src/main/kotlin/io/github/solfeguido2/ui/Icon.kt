package io.github.solfeguido2.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.github.solfeguido2.enums.IconName
import ktx.scene2d.Scene2DSkin

class Icon(icon: IconName) : Group() {

    override fun getHeight() = label.height * scaleY
    override fun getWidth() = label.width * scaleX
    override fun setColor(color: Color?) {
        label.color = color
    }

    override fun getColor(): Color = label.color

    override fun setDebug(enabled: Boolean) {
        label.debug = enabled
    }

    override fun getDebug(): Boolean = label.debug

    val label = Label(icon.value, Scene2DSkin.defaultSkin, "bigIconStyle").also {
        this.addActor(it)
    }

    fun pack() = label.pack()

    fun empty() = label.setText("")

    fun setIcon(icon: IconName) = label.setText(icon.value)
}
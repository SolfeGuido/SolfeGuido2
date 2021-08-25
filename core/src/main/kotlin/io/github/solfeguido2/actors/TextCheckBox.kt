package io.github.solfeguido2.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.Scene2DSkin

class TextCheckBox(text: String) : CheckBox(text, Scene2DSkin.defaultSkin), KTable {

    init {
        setOrigin(Align.center)
        onChange {
            val scale = if(isChecked) 0.7f else 1f
            isTransform = true
            this += Actions.scaleTo(scale, scale, 0.2f, Interpolation.exp10Out)
        }
        pad(10f)
    }
}
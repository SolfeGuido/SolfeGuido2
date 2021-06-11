package io.github.solfeguido.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.enums.IconName
import ktx.actors.onChange
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.Scene2DSkin

class IconCheckBox(val icon: IconName) : CheckBox(icon.value, Scene2DSkin.defaultSkin, "icon"), KTable {

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
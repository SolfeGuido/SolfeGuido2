package io.github.solfeguido.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import io.github.solfeguido.enums.IconName
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.Scene2DSkin

class IconCheckBox<T>(val icon: IconName,  val data : T ) : CheckBox(icon.value, Scene2DSkin.defaultSkin, "icon"), KTable {

    override fun setChecked(isChecked: Boolean) {
        super.setChecked(isChecked)
        print(isChecked)
        val color = if(isChecked) Color.WHITE else Color.BLACK
        this += Actions.color(color, 0.2f, Interpolation.exp10Out)
    }


}
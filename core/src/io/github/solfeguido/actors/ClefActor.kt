package io.github.solfeguido.actors

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.utils.gCol
import ktx.scene2d.Scene2DSkin

class ClefActor(val clef: ClefEnum) : Group() {

    private val label: Label

    override fun getHeight(): Float = label.height
    override fun getWidth(): Float = label.width

    init {
        label = when(clef) {
            ClefEnum.GClef -> getClefLabel(IconName.GClef)
            ClefEnum.FClef -> getClefLabel(IconName.FClef)
            ClefEnum.CClef3 -> getClefLabel(IconName.CClef)
            ClefEnum.CClef4 -> getClefLabel(IconName.CClef)
            else -> Label("", Scene2DSkin.defaultSkin)
        }
    }


    private fun getClefLabel(icon: IconName) = Label(icon.value, Scene2DSkin.defaultSkin, "bigIconStyle").also {
        this.addActor(it)
        it.color = gCol("font")
    }
}
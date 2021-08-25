package io.github.solfeguido2.actors

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.factories.gCol
import ktx.scene2d.Scene2DSkin

class ClefActor(val clef: ClefEnum) : Group() {

    private val label: Label

    val absoluteHeight get() = label.height

    override fun getHeight(): Float = label.height * scaleY
    override fun getWidth(): Float = label.width * scaleX

    init {
        label = when (clef) {
            ClefEnum.GClef -> getClefLabel(IconName.GClef)
            ClefEnum.FClef -> getClefLabel(IconName.FClef)
            ClefEnum.CClef3 -> getClefLabel(IconName.CClef)
            ClefEnum.CClef4 -> getClefLabel(IconName.CClef)
        }
    }


    private fun getClefLabel(icon: IconName) =
        Label(icon.value, Scene2DSkin.defaultSkin, "bigIconStyle").also {
            this.addActor(it)
            it.color = gCol("font")
        }
}
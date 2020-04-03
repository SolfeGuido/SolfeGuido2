package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import io.github.solfeguido.config.ClefConfig
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.utils.colorDrawable
import io.github.solfeguido.utils.gCol
import ktx.scene2d.Scene2DSkin


class Measure(clef: ClefEnum = ClefEnum.GClef, keySignature: KeySignatureEnum = KeySignatureEnum.CMajor) : WidgetGroup() {

    private val line: Drawable = colorDrawable(1, 1, gCol("font"))
    private val clefLabel: Label
    private val clefPosition: ClefConfig

    init {
        clefLabel = when(clef) {
            ClefEnum.GClef -> getClefLabel(IconName.GClef)
            ClefEnum.FClef -> getClefLabel(IconName.FClef)
            ClefEnum.CClef3 -> getClefLabel(IconName.CClef)
            ClefEnum.CClef4 -> getClefLabel(IconName.CClef)
            else -> Label("", Scene2DSkin.defaultSkin)
        }
        clefPosition = ClefConfig.ClefEquivalent[clef, ClefConfig.GClef]
    }

    private fun getClefLabel(icon: IconName) = Label(icon.value, Scene2DSkin.defaultSkin, "bigIconStyle").also {
        val group = Group()
        group.addActor(it)
        this.addActor(group)
        it.color = gCol("font")
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val interSpace = height / 11
        val startPadding = interSpace * 3.5f
        val baseY = y + startPadding
        for(i in 0..4) {
            line.draw(batch, x, baseY + (i*interSpace), width, 2f)
        }
        with(clefLabel.parent) {
            setScale(interSpace / clefLabel.height * clefPosition.height )
            y = interSpace * clefPosition.baseLine
        }

        super.draw(batch, parentAlpha)
    }
}
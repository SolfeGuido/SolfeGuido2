package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.core.music.NoteAccidentalEnum
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.ScaleEnum
import io.github.solfeguido.utils.colorDrawable
import io.github.solfeguido.utils.gCol
import ktx.log.info
import ktx.scene2d.Scene2DSkin


class Measure(clef: ClefEnum = ClefEnum.GClef, keySignature: KeySignatureEnum = KeySignatureEnum.CMajor) : WidgetGroup() {

    private val line: Drawable = colorDrawable(1, 1, gCol("font"))
    private val clefLabel: Label


    init {
        clefLabel = when(clef) {
            ClefEnum.GClef -> getClefLabel(IconName.GClef)
            ClefEnum.FClef -> getClefLabel(IconName.FClef)
            ClefEnum.CClef3 -> getClefLabel(IconName.CClef3)
            ClefEnum.CClef4 -> getClefLabel(IconName.CClef4)
            else -> Label("", Scene2DSkin.defaultSkin)
        }
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
            setScale(interSpace / clefLabel.height * 7.7f )
            y = interSpace * 1.9f
        }

        super.draw(batch, parentAlpha)
    }
}
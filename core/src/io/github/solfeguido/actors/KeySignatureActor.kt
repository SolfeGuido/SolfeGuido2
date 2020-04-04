package io.github.solfeguido.actors

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.core.music.NoteAccidentalEnum
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.utils.gCol
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.collections.toGdxArray
import ktx.scene2d.Scene2DSkin

class KeySignatureActor(private val measure: MeasureActor) : WidgetGroup() {

    private var totalHeight = 0f

    private val accidentals: GdxArray<Label>

    override fun getHeight() = totalHeight

    init {
        val icon = getIcon(measure.keySignature.symbol)
        accidentals = (1..measure.keySignature.numberOf).map {
            val lbl = Label(icon.value, Scene2DSkin.defaultSkin, "bigIconStyle")
            val group = Group()
            group.addActor(lbl)
            lbl.color = gCol("font")
            lbl.pack()
            group.x = 0f//(it * lbl.width)
            group.y = 0f//pos[it-1].toFloat() * (measure.lineSpace / 2f)
            totalHeight = lbl.height
            addActor(group)
            lbl
        }.toGdxArray()
    }

    override fun layout() {
        super.layout()
        val pos = getSymbolPositions(measure.keySignature.symbol, measure.clef)
        accidentals.forEachIndexed {  idx: Int, lbl: Label ->
            val scale = (measure.lineSpace / lbl.height) * 3
            lbl.parent.x = (idx * lbl.width * scale)
            lbl.parent.y = pos[idx].toFloat() * (measure.lineSpace / 2)
            lbl.parent.setScale(scale)
            totalHeight = lbl.height
        }
    }


    private fun getSymbolPositions(symbol: NoteAccidentalEnum, clef: ClefEnum) = when(symbol) {
        NoteAccidentalEnum.Flat -> KeySignatureConfig.flatOrder
        NoteAccidentalEnum.Sharp -> if(clef === ClefEnum.CClef4) KeySignatureConfig.cClef4Order else KeySignatureConfig.sharpOrder
        else -> gdxArrayOf()
    }

    private fun getIcon(symbol: NoteAccidentalEnum) = when(symbol) {
        NoteAccidentalEnum.Sharp -> IconName.SharpAccidental
        NoteAccidentalEnum.Flat -> IconName.FlatAccidental
        NoteAccidentalEnum.Natural -> IconName.Empty
    }


}
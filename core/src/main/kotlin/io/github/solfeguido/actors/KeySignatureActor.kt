package io.github.solfeguido.actors

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import io.github.solfeguido.structures.KeySignatureConfig
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.factories.gCol
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.collections.toGdxArray
import ktx.scene2d.Scene2DSkin
import kotlin.math.max
import kotlin.math.min

class KeySignatureActor(private val measure: MeasureActor) : WidgetGroup() {

    private var totalHeight = 0f
    private var totalWidth = 0f

    private val accidentals: GdxArray<Label>

    override fun getHeight() = totalHeight
    override fun getWidth() = totalWidth

    init {
        val icon = KeySignatureConfig.getIcon(measure.keySignature.symbol)
        accidentals = (1..measure.keySignature.numberOf).map {
            val lbl = Label(icon.value, Scene2DSkin.defaultSkin, "bigIconStyle")
            val group = Group()
            group.addActor(lbl)
            lbl.color = gCol("font")
            lbl.pack()
            group.x = 0f
            group.y = 0f
            totalHeight = lbl.height
            addActor(group)
            lbl
        }.toGdxArray()
    }

    override fun layout() {
        super.layout()
        val pos = getSymbolPositions(measure.keySignature.symbol, measure.clef)
        var minX = Float.MAX_VALUE
        var maxX = 0f
        accidentals.forEachIndexed {  idx: Int, lbl: Label ->
            val scale = (measure.lineSpace / lbl.height) * 3
            lbl.parent.x = (idx * lbl.width * scale)
            maxX = max(maxX, lbl.parent.x)
            minX = min(minX, lbl.parent.y)
            lbl.parent.y = pos[idx].toFloat() * (measure.lineSpace / 2)
            lbl.parent.setScale(scale)
            totalHeight = lbl.height
        }
        totalWidth = max(maxX - minX, 0f)
    }


    private fun getSymbolPositions(symbol: NoteAccidentalEnum, clef: ClefEnum) = when(symbol) {
        NoteAccidentalEnum.Flat -> KeySignatureConfig.FLAT_ORDER
        NoteAccidentalEnum.Sharp -> if(clef === ClefEnum.CClef4) KeySignatureConfig.CCLEF4_ORDER else KeySignatureConfig.SHARP_ORDER
        else -> gdxArrayOf()
    }


}
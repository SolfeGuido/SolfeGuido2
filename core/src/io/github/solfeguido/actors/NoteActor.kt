package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.config.ClefConfig
import io.github.solfeguido.config.Constants
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.core.MidiNote
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.ui.Icon
import ktx.log.info

class NoteActor : WidgetGroup(), Pool.Poolable {

    private var measure: MeasureActor? = null
    var note: MidiNote? = null
        private set
    private var accidental: NoteAccidentalEnum = NoteAccidentalEnum.Natural
    private var relativeMeasurePosition = 0
    // Might be a bug here when changing the app's theme
    private var lineTexture: Drawable = colorDrawable(1, 1, gCol("font"))

    override fun getHeight() = noteIcon.height * scaleY
    override fun getWidth() = (noteIcon.width + accidentalIcon.width)  * scaleX

    private val accidentalIcon = Icon(IconName.Empty).also {
        addActor(it)
        it.debug = true
    }
    private val noteIcon = Icon(IconName.QuarterNote).also {
        addActor(it)
        it.debug = true
    }

    override fun layout() {
        super.layout()
        accidentalIcon.setScale(1/4f)
        noteIcon.x = accidentalIcon.width
        setScale( (measure!!.lineSpace / noteIcon.height) * 4)
        // TODO: Rotate if too high
        this.y = getYIndex()
    }

    private fun getYIndex(): Float{
        val minNote = ClefConfig.ClefMinNote[measure!!.clef]
        relativeMeasurePosition = note!!.getMeasurePosition(minNote, measure!!.keySignature)
        return relativeMeasurePosition * (measure!!.lineSpace / 2)
    }

    override fun reset() {
        note?.let { Pools.free(it) }
        note = null
        measure = null
        relativeMeasurePosition = 0
        accidental = NoteAccidentalEnum.Natural
        noteIcon.color = TRANSPARENT
        accidentalIcon.color = TRANSPARENT
        noteIcon.empty()
        accidentalIcon.empty()
    }

    fun create(measureActor: MeasureActor, note: MidiNote) {
        this.note = note
        this.measure = measureActor
        noteIcon.color = gCol("font")
        accidentalIcon.color = gCol("font")
        accidental =  KeySignatureConfig.getNoteAccidental(note, measure!!.keySignature)
        accidentalIcon.setIcon(KeySignatureConfig.getIcon(accidental))
        accidentalIcon.pack()
        noteIcon.setIcon(IconName.QuarterNote)
        noteIcon.pack()
        noteIcon.x = accidentalIcon.width
        this.x = Gdx.graphics.width.toFloat() - 100f
    }

    private fun drawLine(batch: Batch, y: Float) {
        lineTexture.draw(batch, x-width / 4, y, width * 1.5f, Constants.LINE_THICKNESS)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        //Draw bars
        val topLine = measure!!.topLine
        val lineSpace = measure!!.lineSpace
        val bottom = measure!!.bottomLine

        if(relativeMeasurePosition <= 4) drawLine(batch, bottom - lineSpace)
        if(relativeMeasurePosition <= 2) drawLine(batch, bottom - lineSpace * 2)
        if(relativeMeasurePosition == 0) drawLine(batch, bottom - lineSpace * 3)
        if(relativeMeasurePosition >= 16) drawLine(batch, topLine)
        if(relativeMeasurePosition >= 18) drawLine(batch, topLine + lineSpace)
        if(relativeMeasurePosition >= 20) drawLine(batch, topLine + lineSpace * 2)


        super.draw(batch, parentAlpha)
    }

}
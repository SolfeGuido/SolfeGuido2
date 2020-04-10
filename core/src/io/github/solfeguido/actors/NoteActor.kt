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
    private var note: MidiNote? = null
    private var accidental: NoteAccidentalEnum = NoteAccidentalEnum.Natural
    private var relativeMeasurePosition = 0
    // Might be a bug here when changing the app's theme
    private var lineTexture: Drawable = colorDrawable(1, 1, gCol("font"))

    override fun getHeight() = noteIcon.height
    override fun getWidth() = noteIcon.width

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
        val accidentalBump = when(accidental) {
            NoteAccidentalEnum.Sharp -> -1
            NoteAccidentalEnum.Flat -> 1
            else -> 0
        }
        relativeMeasurePosition = note!!.midiIndex - minNote + accidentalBump
        return (accidentalBump + MidiNote.measurePosition(minNote, note!!.midiIndex)) * (measure!!.lineSpace / 2)
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
        lineTexture.draw(batch, x - width / 4, y, width, Constants.LINE_THICKNESS)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        //Draw bars
        val topLine = measure!!.topLine
        val lineSpace = measure!!.lineSpace
        val bottom = measure!!.bottomLine

        if(relativeMeasurePosition < 9) drawLine(batch, bottom - lineSpace)
        if(relativeMeasurePosition < 6) drawLine(batch, bottom - lineSpace * 2)
        if(relativeMeasurePosition < 2) drawLine(batch, bottom - lineSpace * 3)
        if(relativeMeasurePosition > 27) drawLine(batch, topLine)
        if(relativeMeasurePosition > 30) drawLine(batch, topLine + lineSpace)
        if(relativeMeasurePosition > 34) drawLine(batch, topLine + lineSpace * 2)

        super.draw(batch, parentAlpha)
    }

}
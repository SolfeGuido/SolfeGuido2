package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.config.ClefConfig
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.core.MidiNote
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.ui.Icon
import ktx.log.info

class NoteActor : WidgetGroup(), Pool.Poolable {

    private var measure: MeasureActor? = null
    private var note: MidiNote? = null


    override fun getHeight() = noteIcon.height

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
        this.y = (note!!.midiIndex - ClefConfig.ClefMinNote[measure!!.clef]) * (measure!!.lineSpace / 2)
        info { "${this.y}" }
    }

    override fun reset() {
        note?.let { Pools.free(it) }
        note = null
        measure = null
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
        accidentalIcon.setIcon(KeySignatureConfig.getIcon(getAccidental()))
        accidentalIcon.pack()
        noteIcon.setIcon(IconName.QuarterNote)
        noteIcon.pack()
        noteIcon.x = accidentalIcon.width
        this.x = Gdx.graphics.width.toFloat() - 100f

    }

    private fun getAccidental() = KeySignatureConfig.getNoteAccidental(note!!, measure!!.keySignature)
}
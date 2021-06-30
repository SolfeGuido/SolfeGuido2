package io.github.solfeguido.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.config.Constants
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.core.MidiNote
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.factories.Shaders
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.ui.Icon
import ktx.actors.plusAssign
import ktx.graphics.copy
import ktx.scene2d.Scene2DSkin

class NoteActor : WidgetGroup(), Pool.Poolable {

    private var measure: MeasureActor? = null
    var note: MidiNote? = null
        private set
    private var accidental: NoteAccidentalEnum = NoteAccidentalEnum.Natural
    private var relativeMeasurePosition = 10 // Middle of the

    // Might be a bug here when changing the app's theme
    private var lineTexture: Drawable = colorDrawable(gCol("font"))
    private var consumed = false

    override fun getHeight() = noteIcon.height * scaleY
    override fun getWidth() = (noteIcon.width + accidentalIcon.width) * scaleX

    private val accidentalIcon = Icon(IconName.Empty).also {
        addActor(it)
        it.debug = true
    }
    private val noteIcon = Icon(IconName.QuarterNote).also {
        addActor(it)
        it.debug = true
    }

    private val accidentalEffect = Icon(IconName.Empty)
    private val noteEffect = Icon(IconName.QuarterNote)
    private val noteName = Label("", Scene2DSkin.defaultSkin, "noteNameStyle").also {
        addActor(it)
        it.isVisible = false
        it.debug = true
    }


    override fun layout() {
        super.layout()
        accidentalIcon.setScale(1 / 4f)
        accidentalEffect.setScale(1 / 4f)
        setScale((measure!!.lineSpace / noteIcon.height) * 4)
        this.y = getYIndex()
        noteIcon.x = accidentalIcon.width
        if (relativeMeasurePosition >= 10) {
            accidentalIcon.y -= accidentalIcon.height * 0.2f
            noteIcon.originX = noteIcon.width / 2f
            noteIcon.originY = height * 0.1f
            noteEffect.originX = noteEffect.width / 2f
            noteEffect.originY = height * 0.1f
            noteIcon.rotation = 180f
            noteEffect.rotation = 180f
        }
    }

    private fun getYIndex(): Float {
        val minNote = measure!!.clef.minNote
        relativeMeasurePosition = note!!.getMeasurePosition(minNote, measure!!.keySignature)
        return relativeMeasurePosition * (measure!!.lineSpace / 2)
    }

    override fun reset() {
        note?.let { Pools.free(it) }
        note = null
        measure = null
        noteName.isVisible = false
        relativeMeasurePosition = 10
        accidental = NoteAccidentalEnum.Natural
        noteIcon.apply {
            color = TRANSPARENT
            empty()
        }
        accidentalIcon.apply {
            color = TRANSPARENT
            empty()
        }
        noteEffect.apply {
            empty()
            color = gCol("font")
            setScale(1f)
            remove()
        }
        accidentalEffect.apply {
            empty()
            color = gCol("font")
            setScale(1f)
            remove()
        }
    }

    fun create(measureActor: MeasureActor, note: MidiNote) {
        this.note = note
        this.measure = measureActor
        noteIcon.color = gCol("font")
        accidentalIcon.color = gCol("font")
        accidental = KeySignatureConfig.getNoteAccidental(note, measure!!.keySignature)
        val accIcon = KeySignatureConfig.getIcon(accidental)
        accidentalIcon.setIcon(accIcon)
        accidentalIcon.pack()
        // Updates the size of the accidental icon
        this.pack()
        accidentalEffect.setIcon(accIcon)
        accidentalEffect.pack()
        noteIcon.setIcon(IconName.QuarterNote)
        noteIcon.pack()
        noteEffect.setIcon(IconName.QuarterNote)
        noteEffect.pack()
        //TODO: Change name to be based on the user's preferences
        noteName.setText(note.getName(measureActor.keySignature).value)
        noteName.x = noteIcon.width + accidentalIcon.width
        noteName.pack()
        noteName.y = if (relativeMeasurePosition % 2 == 1) 0f else -measureActor.lineSpace
        this.x = Constants.WIDTH.toFloat()
    }

    fun consume(correct: Boolean) {
        val color = if (correct) gCol("correct") else gCol("error")
        consumed = true
        if (correct) {
            noteEffect.color = color
            accidentalEffect.color = color
            noteEffect.setOrigin(noteIcon.width / 2f, noteIcon.height / 7f)
            accidentalEffect.setOrigin(accidentalIcon.width / 2f, accidentalIcon.height / 7f)

            addActor(noteEffect)
            addActor(accidentalEffect)
            noteEffect += Actions.parallel(
                Actions.color(TRANSPARENT, Constants.FADEOUT_DURATION, Interpolation.circleOut),
                Actions.scaleBy(3f, 3f, Constants.FADEOUT_DURATION, Interpolation.circleOut)
            )
            accidentalEffect += Actions.parallel(
                Actions.color(TRANSPARENT, Constants.FADEOUT_DURATION, Interpolation.circleOut),
                Actions.scaleBy(3f, 3f, Constants.FADEOUT_DURATION, Interpolation.circleOut)
            )
        } else {
            noteIcon.setIcon(IconName.GhostNote)
            noteName.isVisible = true
        }

        fadeOutTo(color)
    }

    private fun fadeOutTo(color: Color) {
        noteIcon += Actions.color(color, 0.1f, Interpolation.exp10Out)
        accidentalIcon += Actions.color(color, 0.1f, Interpolation.exp10Out)
        this += Actions.color(this.color.copy(alpha = 0f), 1f, Interpolation.linear)
    }

    fun simpleFadeOut() {
        noteIcon.actions.clear()
        accidentalIcon.actions.clear()
        fadeOutTo(this.color)
    }

    private fun drawLine(batch: Batch, y: Float) {
        lineTexture.draw(batch, x - width / 4, y, width * 1.5f, Constants.LINE_THICKNESS)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        //Draw bars
        val topLine = measure?.topLine ?: 0f
        val lineSpace = measure?.lineSpace ?: 0f
        val bottom = measure?.bottomLine ?: 0f

        val shader = Shaders.NoteFade

        shader.bind()
        batch.color = color
        batch.shader = shader
        shader.setUniformf("leftLimit", measure!!.leftLimit + 50)
        shader.setUniformf("rightLimit", Constants.WIDTH - 50f)
        shader.setUniformf("noteWidth", width)

        if (relativeMeasurePosition == 0) drawLine(batch, bottom - lineSpace * 3)
        if (relativeMeasurePosition <= 2) drawLine(batch, bottom - lineSpace * 2)
        if (relativeMeasurePosition <= 4) drawLine(batch, bottom - lineSpace)
        if (relativeMeasurePosition >= 16) drawLine(batch, topLine)
        if (relativeMeasurePosition >= 18) drawLine(batch, topLine + lineSpace)
        if (relativeMeasurePosition >= 20) drawLine(batch, topLine + lineSpace * 2)

        super.draw(batch, parentAlpha)
        batch.shader = null
    }

}
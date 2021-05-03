package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
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
import io.github.solfeguido.factories.Shaders
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.ui.Icon
import ktx.actors.plusAssign

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


    override fun layout() {
        super.layout()
        accidentalIcon.setScale(1 / 4f)
        accidentalEffect.setScale(1 / 4f)
        noteIcon.x = accidentalIcon.width
        setScale((measure!!.lineSpace / noteIcon.height) * 4)
        // TODO: Rotate if too high
        this.y = getYIndex()
    }

    private fun getYIndex(): Float {
        val minNote = ClefConfig.ClefMinNote[measure!!.clef]
        relativeMeasurePosition = note!!.getMeasurePosition(minNote, measure!!.keySignature)
        return relativeMeasurePosition * (measure!!.lineSpace / 2)
    }

    override fun reset() {
        note?.let { Pools.free(it) }
        note = null
        measure = null
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
            this@NoteActor.removeActor(this)
            empty()
            color = gCol("font")
            setScale(1f)
        }
        accidentalEffect.apply {
            this@NoteActor.removeActor(this)
            empty()
            color = gCol("font")
            setScale(1f)
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
        accidentalEffect.setIcon(accIcon)
        accidentalEffect.pack()
        noteIcon.setIcon(IconName.QuarterNote)
        noteIcon.pack()
        noteEffect.setIcon(IconName.QuarterNote)
        noteEffect.pack()
        noteIcon.x = accidentalIcon.width
        noteEffect.x = accidentalIcon.width
        this.x = Gdx.graphics.width.toFloat()
    }

    private fun consume(correct: Boolean) {
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
                Actions.color(TRANSPARENT, 2f, Interpolation.circleOut),
                Actions.scaleTo(2f, 2f, 2f, Interpolation.circleOut)
            )
            accidentalEffect += Actions.parallel(
                Actions.color(TRANSPARENT, 2f, Interpolation.circleOut),
                Actions.scaleTo(0.5f, 0.5f, 2f, Interpolation.circleOut)
            )
        } else {
            noteIcon.setIcon(IconName.GhostNote)
        }

        noteIcon += Actions.color(color, 0.2f, Interpolation.exp10Out)
        accidentalIcon += Actions.color(color, 0.2f, Interpolation.exp10Out)
    }

    fun setCorrect() {
        consume(true)
    }

    fun setWrong() {
        consume(false)
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
        batch.shader = shader
        shader.setUniform2fv(
            "u_viewport_size",
            floatArrayOf(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()),
            0,
            2
        )
        shader.setUniformf("leftLimit", measure!!.leftLimit + 50)
        shader.setUniformf("rightLimit", Gdx.graphics.width.toFloat() - 50)
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
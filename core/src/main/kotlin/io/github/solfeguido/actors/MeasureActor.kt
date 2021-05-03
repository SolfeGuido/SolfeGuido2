package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import io.github.solfeguido.config.ClefConfig
import io.github.solfeguido.config.Constants
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.factories.*
import io.github.solfeguido.ui.events.ResultEvent
import ktx.collections.gdxArrayOf
import kotlin.math.exp
import kotlin.math.max
import kotlin.random.Random

class MeasureActor(
    val clef: ClefEnum = ClefEnum.GClef,
    val keySignature: KeySignatureEnum = KeySignatureEnum.CMajor
) : WidgetGroup() {

    private val line: Drawable = colorDrawable(gCol("font"))
    private var currentNoteIndex = 0
    var lineSpace = 0f
        private set
    var bottomLine = 0f
        private set
    var topLine = 0f
        private set


    private val clefActor: ClefActor = ClefActor(clef).also { addActor(it) }

    val leftLimit
        get() = signatureActor.x + signatureActor.width
    private val clefPosition: ClefConfig = ClefConfig.ClefEquivalent[clef, ClefConfig.GClef]
    private val notes = gdxArrayOf<NoteActor>()

    private val signatureActor = KeySignatureActor(this).also { addActor(it) }
    private val currentNote
        get() = if (notes.isEmpty) NoteActorPool.generate(MidiNotePool.fromIndex(60), this).also {
        notes.add(it)
        addActor(it)
    } else this.notes[currentNoteIndex]

    fun checkNote(note: NoteOrderEnum) {
        val expected = currentNote.note?.noteOrder ?: return
        val event = ResultEvent(expected, note)
        this.fire(event)
        //TODO: add an effect
        if(event.isCorrect)
            currentNote.setCorrect()
        else
            currentNote.setWrong()

        currentNoteIndex++
    }

    override fun act(delta: Float) {
        super.act(delta)
        val current = currentNote
        if (notes.isEmpty) return
        var maxLeft = 0f
        val end = (signatureActor.x + signatureActor.width) + currentNote.width
        val start = Gdx.graphics.width.toFloat() + 100f
        val nwPos = Interpolation.exp10Out.apply(start, end, (start - current.x) / (start - end))
        val moveBy = (current.x - nwPos) * delta
        notes.forEach {
            it.x -= moveBy
            maxLeft = max(maxLeft, it.x)
        }
        if (maxLeft < (this.width - (currentNote.width * 3))) {
            generateNote()
        }
    }

    private fun generateNote() =
        NoteActorPool.generate(MidiNotePool.fromIndex(Random.nextInt(60, 80)), this).also {
            notes.add(it)
            addActor(it)
        }

    // Used only for testing, move the current note to the next semi-tone
    fun nextNote() {
        val idx = (currentNote.note!!.midiIndex + 1)
        currentNote.reset()
        currentNote.create(this, MidiNotePool.fromIndex(idx))
        currentNote.layout()
    }

    // Used only for testing, move the current note to the previous semi-tone
    fun prevNote() {
        val idx = (currentNote.note!!.midiIndex - 1)
        currentNote.reset()
        currentNote.create(this, MidiNotePool.fromIndex(idx))
        currentNote.layout()
    }

    override fun layout() {
        super.layout()
        lineSpace = height / 11
        bottomLine = lineSpace * 3.5f
        topLine = bottomLine + (lineSpace * 5)


        val scale = (lineSpace / clefActor.absoluteHeight) * clefPosition.height
        clefActor.setScale(scale)
        clefActor.y = lineSpace * clefPosition.baseLine
        signatureActor.x = clefActor.width * scale
        signatureActor.y =
            (bottomLine - lineSpace * 1.5f) + KeySignatureConfig.CLEF_TRANSLATE[clef] * (lineSpace / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isTransform) applyTransform(batch, computeTransform())
        for (i in 0..4) line.draw(
            batch,
            0f,
            0f + bottomLine + (i * lineSpace),
            width,
            Constants.LINE_THICKNESS
        )
        if (isTransform) resetTransform(batch)

        super.draw(batch, parentAlpha)
    }
}
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
import kotlin.math.max
import kotlin.random.Random

class MeasureActor(
    val clef: ClefEnum = ClefEnum.GClef,
    val keySignature: KeySignatureEnum = KeySignatureEnum.CMajor
) : WidgetGroup() {

    private val line: Drawable = colorDrawable(gCol("font"))
    var terminated = false
    private var currentNoteIndex = 0
    var lineSpace = 0f
        private set
    var bottomLine = 0f
        private set
    var topLine = 0f
        private set


    val leftLimit
        get() = signatureActor.x + signatureActor.width

    val lastNote
        get() = this.notes.lastOrNull()

    private val clefActor: ClefActor = ClefActor(clef).also { addActor(it) }
    private val clefPosition: ClefConfig = ClefConfig.ClefEquivalent[clef, ClefConfig.GClef]
    private val notes = gdxArrayOf<NoteActor>()

    private val signatureActor = KeySignatureActor(this).also { addActor(it) }

    fun checkNote(note: NoteOrderEnum) : Boolean {
        val expected = currentNote().note?.noteOrder ?: return false
        val event = ResultEvent(expected, note)
        this.fire(event)
        currentNote().consume(event.isCorrect)

        currentNoteIndex++
        return event.isCorrect
    }

    override fun act(delta: Float) {
        super.act(delta)
        val current = currentNote()
        var maxLeft = 0f
        val end = (signatureActor.x + signatureActor.width) + current.width
        val start = Gdx.graphics.width.toFloat() + 100f
        val nwPos = Interpolation.exp10Out.apply(start, end, (start - current.x) / (start - end))
        val moveBy = (current.x - nwPos) * delta

        val clearNotes = mutableSetOf<NoteActor>()
        notes.forEach {
            it.x -= moveBy
            if(it.x < -this.width) {
                clearNotes.add(it)
            }
            maxLeft = max(maxLeft, it.x)
        }
        clearNotes.forEach {
            notes.removeValue(it, true)
            this.removeActor(it)
            currentNoteIndex--
        }

        if (!terminated && maxLeft < (this.width - (current.width * 3))) {
            generateNote()
        }

    }

    private fun currentNote() =
        if (notes.isEmpty || currentNoteIndex >= notes.size) NoteActorPool.generate(
            MidiNotePool.fromIndex(
                60
            ), this
        ).also {
            notes.add(it)
            addActor(it)
        } else this.notes[currentNoteIndex]

    /**
     * Whenever a new note is added to this group
     * 'invalidateHierarchy' is called. This may
     * invalidate the parent table element. When it does so,
     * the table will try to reposition all of its child elements.
     * And by doing so, it might reposition some elements that are
     * currently transition to the left or the right of the screen
     * This produces a weird flickering on the screen, and ultimately
     * it will put back on the screen a menu that should be hidden
     * In order to avoid the hierarchy invalidation to be called
     * we just need to tell this function to do nothing. Because
     * when adding a new note to the measure, the measure itself will
     * not change its size, so no need to update any elements at all.
     */
    override fun childrenChanged() {}

    private fun generateNote() =
        NoteActorPool.generate(MidiNotePool.fromIndex(Random.nextInt(80, 90)), this).also {
            notes.add(it)
            addActor(it)
        }

//    // Used only for testing, move the current note to the next semi-tone
//    fun nextNote() {
//        val idx = (currentNote.note!!.midiIndex + 1)
//        currentNote.reset()
//        currentNote.create(this, MidiNotePool.fromIndex(idx))
//        currentNote.layout()
//    }
//
//    // Used only for testing, move the current note to the previous semi-tone
//    fun prevNote() {
//        val idx = (currentNote.note!!.midiIndex - 1)
//        currentNote.reset()
//        currentNote.create(this, MidiNotePool.fromIndex(idx))
//        currentNote.layout()
//    }

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

    fun terminate() {
        terminated = true
        notes.forEach {
            it.simpleFadeOut()
        }
    }
}
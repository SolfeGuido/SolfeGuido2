package io.github.solfeguido2.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.structures.KeySignatureConfig
import io.github.solfeguido2.enums.NoteNameEnum
import io.github.solfeguido2.enums.NoteOrderEnum
import io.github.solfeguido2.enums.NoteStyle
import io.github.solfeguido2.factories.*
import io.github.solfeguido2.settings.MeasureSettings
import io.github.solfeguido2.events.ResultEvent
import ktx.collections.gdxArrayOf
import kotlin.math.max

class MeasureActor(settings: MeasureSettings, private val noteStyle: NoteStyle) : WidgetGroup() {

    val clef = settings.clef
    val keySignature = settings.signature
    private val generator = settings.generator
    private var highlightedNote: NoteActor? = null
    private var speedMultiplier = 1f

    private val lineDrawable = colorDrawable(gCol("font"))
    private val highlighterDrawable = colorDrawable(gCol("stripe"))

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

    private val clefActor: ClefActor = ClefActor(clef).also { addActor(it) }
    private val notes = gdxArrayOf<NoteActor>()

    private val signatureActor = KeySignatureActor(this).also { addActor(it) }

    fun nameNote(note: NoteNameEnum) = NoteNameEnum[note.name, noteStyle]

    fun checkNote(note: NoteOrderEnum): Boolean {
        val expected = currentNote().note ?: return false
        val isCorrect = withPooled<ResultEvent, Boolean> { result ->
            result.expected = expected
            result.actual = note
            val res = result.isCorrect
            this.fire(result)
            res
        }
        currentNote().consume(isCorrect)
        currentNoteIndex++
        return isCorrect
    }

    fun pause() {
        speedMultiplier = 0f
    }

    fun resume() {
        speedMultiplier = 1f
    }

    override fun act(delta: Float) {
        super.act(delta)
        val current = currentNote()
        var maxLeft = 0f
        val end = (signatureActor.x + signatureActor.width) + current.width
        val start = Constants.WIDTH + 100f
        val nwPos = Interpolation.exp10Out.apply(start, end, (start - current.x) / (start - end))
        val moveBy = (current.x - nwPos) * delta * speedMultiplier

        val clearNotes = mutableSetOf<NoteActor>()
        notes.forEach {
            it.x -= moveBy
            if (it.x < -this.width) {
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

    fun currentNote() =
        if (notes.isEmpty || currentNoteIndex >= notes.size) {
            generateNote()
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
        NoteActorPool.generate(MidiNotePool.fromIndex(generator.next()), this).also {
            notes.add(it)
            addActor(it)
        }

    fun highlightCurrentNote() {
        highlightedNote = currentNote()
    }

    fun lowlightCurrentNote() {
        highlightedNote = null
    }

    override fun layout() {
        super.layout()
        lineSpace = height / 11
        bottomLine = lineSpace * 3.5f
        topLine = bottomLine + (lineSpace * 5)


        val scale = (lineSpace / clefActor.absoluteHeight) * clef.height
        clefActor.setScale(scale)
        clefActor.y = lineSpace * clef.iconBaseLine
        signatureActor.x = clefActor.width * scale
        signatureActor.y =
            (bottomLine - lineSpace * 1.5f) + KeySignatureConfig.CLEF_TRANSLATE[clef] * (lineSpace / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isTransform) applyTransform(batch, computeTransform())
        for (i in 0..4) lineDrawable.draw(
            batch,
            0f,
            0f + bottomLine + (i * lineSpace),
            width,
            Constants.LINE_THICKNESS
        )
        if (isTransform) resetTransform(batch)

        super.draw(batch, parentAlpha)
        highlightedNote?.let {
            highlighterDrawable.draw(batch, it.leftMostX, this.y, it.rightMostX, this.height)
        }
    }

    fun terminate() {
        terminated = true
        highlightedNote = null
        notes.forEach {
            it.simpleFadeOut()
        }
    }
}
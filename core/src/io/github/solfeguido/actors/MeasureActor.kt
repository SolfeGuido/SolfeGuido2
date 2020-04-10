package io.github.solfeguido.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Pools
import com.badlogic.gdx.utils.Timer
import io.github.solfeguido.config.ClefConfig
import io.github.solfeguido.config.Constants
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.factories.*
import ktx.collections.gdxListOf
import ktx.log.info


class MeasureActor(val clef: ClefEnum = ClefEnum.GClef, val keySignature: KeySignatureEnum = KeySignatureEnum.CMajor) : WidgetGroup() {

    private val line: Drawable = colorDrawable(1, 1, gCol("font"))
    var lineSpace = 0f
        private set
    var bottomLine = 0f
        private set
    var topLine = 0f
        private set
    private val clefActor: ClefActor = ClefActor(clef).also { addActor(it) }
    private val clefPosition: ClefConfig = ClefConfig.ClefEquivalent[clef, ClefConfig.GClef]
    private val signatureActor = KeySignatureActor(this).also { addActor(it) }

    private val notes = gdxListOf<NoteActor>()

    init {
        val min = ClefConfig.ClefMinNote[clef]
        val test = NoteActorPool.generate(MidiNotePool.fromIndex(min), this)
        for(i in min until min + 36) {
            schedule((1f + i - min) * 0.3f) {
                test.reset()
                test.create(this, MidiNotePool.fromIndex(i))
                test.layout()
            }
        }
        notes.add(test)
        addActor(test)
    }

    override fun layout() {
        super.layout()
        lineSpace = height / 11
        bottomLine = lineSpace * 3.5f
        topLine = bottomLine + (lineSpace * 5)

        val scale = (lineSpace / clefActor.height) * clefPosition.height
        clefActor.setScale(scale)
        clefActor.y = lineSpace * clefPosition.baseLine
        signatureActor.x = clefActor.width * scale
        signatureActor.y = (bottomLine - lineSpace * 1.5f)  + KeySignatureConfig.CLEF_TRANSLATE[clef] * (lineSpace / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        for(i in 0..4) line.draw(batch, x, y + bottomLine + (i*lineSpace), width, Constants.LINE_THICKNESS)
        super.draw(batch, parentAlpha)
    }
}
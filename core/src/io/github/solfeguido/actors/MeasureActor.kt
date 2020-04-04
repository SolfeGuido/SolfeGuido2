package io.github.solfeguido.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import io.github.solfeguido.config.ClefConfig
import io.github.solfeguido.config.KeySignatureConfig
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.utils.colorDrawable
import io.github.solfeguido.utils.gCol
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


    override fun layout() {
        super.layout()
        lineSpace = height / 11
        bottomLine = lineSpace * 3.5f
        topLine = bottomLine + (lineSpace * 5)

        val scale = (lineSpace / clefActor.height) * clefPosition.height
        clefActor.setScale(scale)
        clefActor.y = lineSpace * clefPosition.baseLine
        signatureActor.x = clefActor.width * scale
        signatureActor.y = (bottomLine - lineSpace * 1.5f)  + KeySignatureConfig.ClefTranslate[clef] * (lineSpace / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        for(i in 0..4) line.draw(batch, x, y + bottomLine + (i*lineSpace), width, 2f)
        super.draw(batch, parentAlpha)
    }
}
package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol

class BorderLabel(text: String, style: String, skin: Skin) : Label(text, skin, style) {

    val LABEL_BORDER_THICKNESS = 1.5f

    val borderDrawable = colorDrawable(1, 1, gCol("font"))

    init {
        this.style.background = colorDrawable(1, 1, gCol("background"))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {

        borderDrawable.draw(batch, x - LABEL_BORDER_THICKNESS, y - LABEL_BORDER_THICKNESS, width + (LABEL_BORDER_THICKNESS * 2), height + LABEL_BORDER_THICKNESS *2)

        super.draw(batch, parentAlpha)
    }
}
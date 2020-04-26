package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol

class BorderContainer<T : Actor> : Container<T>() {

    private val border = colorDrawable(1, 1, gCol("font"))
    private val borderThickness = 1f

    init {
        background = colorDrawable(1, 1, gCol("background"))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if(isTransform) {
            applyTransform(batch, computeTransform())
            border.draw(batch, -borderThickness, -borderThickness, width + (borderThickness * 2), height + (borderThickness * 2))
            resetTransform(batch)
        } else {
            border.draw(batch, x -borderThickness, y -borderThickness, width + (borderThickness * 2), height + (borderThickness * 2))
        }

        super.draw(batch, parentAlpha)
    }

}
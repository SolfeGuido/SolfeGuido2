package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.scene2d.KGroup
import ktx.scene2d.progressBar

class BorderContainer<T : Actor> : Container<T>(), KGroup {

    private val border = colorDrawable(1, 1, gCol("font"))
    private val borderThickness = 1f

    init {
        background = colorDrawable(1, 1, gCol("background"))
    }

    override fun addActor(actor: Actor?) {
        this.actor == null || throw IllegalStateException("Container may store only a single child.")
        this.actor = actor as T
    }

    override fun layout() {
        super.layout()
        setOrigin(width /2, height /2)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val myCol = color
        batch.setColor(myCol.r, myCol.g, myCol.b, myCol.a * parentAlpha)
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
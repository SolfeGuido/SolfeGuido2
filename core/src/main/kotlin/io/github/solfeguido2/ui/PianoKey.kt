package io.github.solfeguido2.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.enums.PianoKeyTypeEnum
import io.github.solfeguido2.factories.colorDrawable
import io.github.solfeguido2.factories.gCol
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.style.get

class PianoKey(text: String = "", private val type: PianoKeyTypeEnum = PianoKeyTypeEnum.White) :
    Button(Scene2DSkin.defaultSkin.get<TextButton.TextButtonStyle>(type.style)), KTable {

    val BORDER_TICKNESS = 4f


    private val backgroundColor
        get() = if (type == PianoKeyTypeEnum.Black) gCol("font") else gCol("background")


    init {
        if (this.type == PianoKeyTypeEnum.White) {
            label(text, "contentLabelStyle")
        }
        this.isTransform = true
        setupListeners()
    }

    override fun layout() {
        super.layout()
        setOrigin(width / 2f, height)
    }

    fun highlight() {
        this += Actions.color(gCol("correct"), 0.1f) + Actions.color(gCol("reset"), 0.8f)
    }

    private val borderDrawable = colorDrawable(gCol("font"))
    private val backgroundDrawable = colorDrawable(backgroundColor)

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (isTransform) applyTransform(batch, computeTransform())
        val c = color
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha)

        backgroundDrawable.draw(batch, 0f, 0f, width, height)
        borderDrawable.draw(batch, 0f, 0f, width, BORDER_TICKNESS)
        borderDrawable.draw(batch, 0f, height - BORDER_TICKNESS, width, BORDER_TICKNESS)
        borderDrawable.draw(batch, 0f, 0f, BORDER_TICKNESS, height)
        borderDrawable.draw(batch, width - BORDER_TICKNESS, 0f, BORDER_TICKNESS, height)

        if (isTransform) resetTransform(batch)

        super.draw(batch, parentAlpha)
    }

    private fun setupListeners() {
        val self = this
        addListener(object : ClickListener() {

            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                val res = super.touchDown(event, x, y, pointer, button)
                if (self.isDisabled) return res
                self.addAction(
                    Actions.scaleTo(
                        Constants.PIANO_PRESSED_SCALING,
                        Constants.PIANO_PRESSED_SCALING,
                        .2f,
                        Interpolation.exp10Out
                    )
                )
                return res
            }

            override fun touchUp(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ) {
                super.touchUp(event, x, y, pointer, button)
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }

}
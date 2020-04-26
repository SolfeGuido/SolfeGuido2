package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.scene2d.KTable
import ktx.scene2d.addTextTooltip
import ktx.scene2d.addTooltip
import ktx.scene2d.label
import ktx.style.get

class BorderButton(text: String, style: TextButton.TextButtonStyle): Button(), KTable {

    private val BUTTON_BORDER = 1.5f

    //TODO : style options

    var disableOnPressed = false

    private val borderDrawable = colorDrawable(1, 1,gCol("font"))
    private val backgroundDrawable = colorDrawable(1, 1, gCol("background"))


    constructor(text: String, style: String, skin: Skin): this(text, skin.get<TextButton.TextButtonStyle>(style))

    init {
        setStyle(style)
        label(text) {
            setStyle(Label.LabelStyle(style.font, Color(Color.WHITE)))
            setAlignment(Align.center)
            addAction(Actions.color(gCol("font")))
        }.inCell.expand().fill()
        initialize()
        this.isTransform = true
    }

    override fun layout() {
        super.layout()
        this.setOrigin(width / 2f, height / 2f)
    }


    override fun draw(batch: Batch, parentAlpha: Float) {
        applyTransform(batch, computeTransform())
        batch.color.a *= parentAlpha
        backgroundDrawable.draw(batch, 0f, 0f, width, height )
        borderDrawable.draw(batch, 0f, 0f, width, BUTTON_BORDER)
        borderDrawable.draw(batch, 0f, 0f + height - BUTTON_BORDER, width, BUTTON_BORDER)
        borderDrawable.draw(batch, 0f, 0f, BUTTON_BORDER, height)
        borderDrawable.draw(batch, 0f + width - BUTTON_BORDER, 0f, BUTTON_BORDER, height)
        resetTransform(batch)

        super.draw(batch, parentAlpha)
    }

    private fun initialize(){
        val self = this
        addListener(object: ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                if(self.isDisabled) return
                self.addAction(Actions.color(gCol("fontHover"), 0.2f))
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                self.addAction(Actions.color(gCol("font"), 0.2f))
            }

            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                self.isDisabled = self.disableOnPressed
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                val res = super.touchDown(event, x, y, pointer, button)
                if(self.isDisabled) return res
                self.addAction(Actions.scaleTo(Constants.PRESSED_SCALING, Constants.PRESSED_SCALING, .2f, Interpolation.exp10Out))
                return res
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }
}
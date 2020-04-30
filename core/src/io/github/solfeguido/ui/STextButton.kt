package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.config.Constants
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import  ktx.style.get
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.label
import ktx.scene2d.progressBar
import ktx.style.defaultStyle

@Scene2dDsl
class STextButton(text: String, buttonStyle: STextButtonStyle) : Button(buttonStyle), KTable {

    val label: Label
    var disableOnPressed = false

    private val style: STextButtonStyle = buttonStyle
    private var borderDrawable: Drawable
    private var backgroundDrawable: Drawable

    constructor(text: String, style: String, skin: Skin): this(text, skin.get<STextButtonStyle>(style))

    fun setBackgroundColor(color: Color) {
        this += Actions.color(color, 0.5f, Interpolation.exp10Out)
    }
    init {
        this.label = label(text, style.labelStyle) {
            setAlignment(Align.center)
            it.expand().fill()
            this += Actions.color(style.fontColor ?: gCol("font"))
        }
        setSize(prefWidth, prefHeight)
        initialize()
        this.isTransform = true
        borderDrawable = colorDrawable(style.borderColor ?: gCol("background"))
        backgroundDrawable = colorDrawable(style.backgroundColor ?: gCol("background"))
    }

    override fun layout() {
        super.layout()
        setOrigin(Align.center)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        applyTransform(batch, computeTransform())
        val c= color
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha)
        if(style.borderThickness > 0f) {
            borderDrawable.draw(batch, -style.borderThickness, -style.borderThickness, width + (style.borderThickness * 2), height + (style.borderThickness * 2))
        }

        backgroundDrawable.draw(batch, 0f, 0f, width, height)

        resetTransform(batch)


        super.draw(batch, parentAlpha)
    }

    private fun initialize(){
        val self = this
        addListener(object: ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                if(self.isDisabled) return
                self.label.addAction(Actions.color(gCol("fontHover"), 0.2f))
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                self.label.addAction(Actions.color(gCol("font"), 0.2f))
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
                if(isDisabled) return
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }


    class STextButtonStyle : ButtonStyle() {

        var fontColor: Color = gCol("font")
        var labelStyle : String = defaultStyle
        var borderColor: Color? = null
        var backgroundColor: Color? = null
        var borderThickness = 1f
    }
}
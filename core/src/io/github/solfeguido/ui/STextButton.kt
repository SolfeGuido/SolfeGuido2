package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
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
import io.github.solfeguido.utils.gCol
import ktx.scene2d.KTable
import  ktx.style.get
import ktx.scene2d.Scene2dDsl

@Scene2dDsl
class STextButton(text: String, style: TextButton.TextButtonStyle) : Button(), KTable {

    val label: Label
    val style: TextButton.TextButtonStyle
    val disableOnPressed = false

    constructor(text: String, style: String, skin: Skin): this(text, skin.get<TextButton.TextButtonStyle>(style))

    init {
        setStyle(style)
        label = Label(text, Label.LabelStyle(style.font, Color(Color.WHITE)))
        label.addAction(Actions.color(gCol("font")))
        this.style = style
        add(label).expand().fill()
        setSize(prefWidth, prefHeight)
        initialize()
        this.isTransform = true
        this.setOrigin(Align.center)
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
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }

}
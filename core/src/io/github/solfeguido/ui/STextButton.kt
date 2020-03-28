package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import io.github.solfeguido.utils.gCol
import ktx.log.info
import ktx.scene2d.KTable
import  ktx.style.get
import ktx.scene2d.Scene2dDsl

@Scene2dDsl
class STextButton(text: String, style: TextButton.TextButtonStyle) : Button(), KTable {

    val label: Label
    val style: TextButton.TextButtonStyle

    constructor(text: String, style: String, skin: Skin): this(text, skin.get<TextButton.TextButtonStyle>(style))

    init {
        setStyle(style)
        label = Label(text, Label.LabelStyle(style.font, Color(Color.WHITE)))
        label.addAction(Actions.color(Color(Color.BLACK)))
        this.style = style
        add(label).expand().fill()
        setSize(prefWidth, prefHeight)
        initialize()
    }

    private fun initialize(){
        val self = this
        addListener(object: ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                self.label.addAction(Actions.color(gCol("fontHover"), 0.2f))
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                self.label.addAction(Actions.color(gCol("font"), 0.2f))
            }
        })
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        // Maybe draw stuff
        super.draw(batch, parentAlpha)
    }

}
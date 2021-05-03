package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.factories.inside
import ktx.actors.div
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.defaultStyle
import ktx.style.get

class ZoomDialog(style: String, skin: Skin) : Dialog("", skin, style) {

    private val background = colorDrawable(Color(0f, 0f, 0f, 0.7f))

    private var addedCross = false
    private var touchedOutside = false

    init {
        // Hide when click/touch outside the
        addListener(object :InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if(!inside(x, y) ) {
                    touchedOutside = true
                    return true
                }
                return false
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                if(!touchedOutside || inside(x, y)) return
                touchedOutside = false
                hide()
            }
        })

    }


    override fun show(stage: Stage): Dialog {
        super.show(stage,
                 (Actions.alpha(0f) / Actions.scaleTo(0f, 0f)) +
                         Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingOut) /
                         Actions.fadeIn(0.4f, Interpolation.fade)
                )
        setPosition((stage.width - width) / 2 , (stage.height - height) / 2)
        setOrigin(Align.center)
        return this
    }

    override fun hide() {
        super.hide(
                Actions.fadeOut(0.4f, Interpolation.fade) /
                        Actions.scaleTo(0f, 0f, 0.4f, Interpolation.exp10Out)
        )
    }

    fun title(text: String): Cell<Label> = Label(text, Scene2DSkin.defaultSkin.get<Label.LabelStyle>(defaultStyle)).let {
        contentTable.add(it).also {
            it.top()
            if(!addedCross) {
                val crossBtn = STextButton(IconName.Times.value, "iconButtonStyle", Scene2DSkin.defaultSkin)
                crossBtn.onClick { hide() }
                contentTable.add(crossBtn).top()
                addedCross = true
            }
            contentTable.row()
        }
    }

    fun line(text: String): Cell<Label> = Label(text, Scene2DSkin.defaultSkin.get<Label.LabelStyle>("contentLabelStyle")).let {
        contentTable.add(it).also { contentTable.row() }
    }

    fun borderButton(text: String, value: Any? = null): Cell<STextButton> = STextButton(text, defaultStyle, Scene2DSkin.defaultSkin).let {
        it.pad(5f)
        setObject(it, value)
        buttonTable.add(it).also { cell -> cell.pad(5f).top()  }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val c = color
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha)
        background.draw(batch, 0f, 0f, stage.width, stage.height)
        super.draw(batch, parentAlpha)
    }

}
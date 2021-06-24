package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.actors.plusAssign
import ktx.scene2d.*
import  ktx.style.get
import ktx.style.defaultStyle

@Scene2dDsl
class STextButton(text: String, buttonStyle: STextButtonStyle) : Button(buttonStyle), KTable {

    val label: Label
    var disableOnPressed = false

    private val style: STextButtonStyle = buttonStyle
    private val mBorderDrawable: Drawable
    private val mBackgroundDrawable: Drawable

    constructor(text: String, style: String, skin: Skin) : this(text, skin.get<STextButtonStyle>(style))

    fun setBackgroundColor(color: Color) {
        this += Actions.color(color, 0.5f, Interpolation.exp10Out)
    }

    fun icon(icon: IconName, scale: Float = 1f): Cell<Group> {
        this.clearChildren()
        val grp = Group()
        val lbl = label(icon.value, "iconStyle") {
            setAlignment(Align.center)
            this += Actions.color(gCol("font"))
        }
        grp.setSize(lbl.width, lbl.height)
        grp.addActor(lbl)
        grp.setScale(scale)
        val cell = add(grp).grow().center()
        lbl.setPosition(lbl.width - grp.width * scale, lbl.height - grp.height * scale)

        add(this.label).pad(0f, 5f, 0f, 5f).grow()
        return cell
    }

    init {
        this.label = label(text, style.labelStyle) {
            setAlignment(Align.center)
            it.grow()
            this += Actions.color(style.fontColor ?: gCol("font"))
        }
        setSize(prefWidth, prefHeight)
        initialize()
        this.isTransform = true
        mBorderDrawable = colorDrawable(style.borderColor ?: gCol("background"))
        mBackgroundDrawable = colorDrawable(style.backgroundColor ?: gCol("background"))
    }

    override fun layout() {
        super.layout()
        setOrigin(Align.center)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        applyTransform(batch, computeTransform())
        val c = color
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha)
        if (style.borderThickness > 0f) {
            mBorderDrawable.draw(batch, -style.borderThickness, -style.borderThickness, width + (style.borderThickness * 2), height + (style.borderThickness * 2))
        }

        mBackgroundDrawable.draw(batch, 0f, 0f, width, height)

        resetTransform(batch)


        super.draw(batch, parentAlpha)
    }

    private fun initialize() {
        val self = this
        addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                if (self.isDisabled) return
                super.enter(event, x, y, pointer, fromActor)
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
                if (self.isDisabled) return false
                val res = super.touchDown(event, x, y, pointer, button)
                self.addAction(Actions.scaleTo(Constants.PRESSED_SCALING, Constants.PRESSED_SCALING, .2f, Interpolation.exp10Out))
                return res
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                if (isDisabled) return
                super.touchUp(event, x, y, pointer, button)
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }

    class STextButtonStyle : ButtonStyle() {

        var fontColor: Color = gCol("font")
        var labelStyle: String = defaultStyle
        var borderColor: Color? = null
        var backgroundColor: Color? = null
        var borderThickness = 1f
    }
}
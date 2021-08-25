package io.github.solfeguido2.ui

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Disableable
import com.badlogic.gdx.utils.Align
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.factories.borderContainer
import io.github.solfeguido2.factories.gCol
import ktx.scene2d.*
import ktx.actors.*

class AnswerButton(note: String) : Stack(), KGroup, Disableable {

    private var _disabled = false
    private var disableOnPressed = false
    private val isIconShown
        get() = currentIcon != IconName.Empty
    private var currentIcon: IconName = IconName.Empty
    private val accidentalLabel: Label
    private val labelParent: BorderContainer<Actor>

    init {
        isTransform = true
        container {
            borderContainer {
                label(note, "contentLabelStyle") {
                    setAlignment(Align.center)
                }
            }.fill()
        }.fill().pad(10f)
        currentIcon = IconName.Empty
        val icon = currentIcon.value
        container {
            this@AnswerButton.labelParent = borderContainer {
                isTransform = true
                this@AnswerButton.accidentalLabel = label(icon, "iconStyle") {
                    color = gCol("font")
                }
                pad(5f)
            }
            top().right().padTop(5f)
        }
        this.labelParent.setScale(0f)

        initialize()
    }

    private fun queueActions(action: Action) {
        val currentActions = labelParent.actions
        if (currentActions.size == 0) {
            labelParent += action
            return
        }
        val first = currentActions.removeIndex(0)
        labelParent += (first + action)
    }

    private fun hideIcon(delay: Float = 0f) {
        if (!isIconShown) return
        currentIcon = IconName.Empty
        val hideAnim = Actions.delay(delay) then Actions.scaleTo(0f, 0f, 0.2f, Interpolation.exp10In)
        queueActions(hideAnim)
    }

    fun toggleIcon(icon: IconName, delay: Float = 0f): IconName {
        if (icon == currentIcon) {
            hideIcon(delay)
            return currentIcon
        }
        val showAnim = if (isIconShown) {
            Actions.delay(delay) then Actions.scaleTo(0f, 0f, 0.2f, Interpolation.exp10In) then Actions.run {
                accidentalLabel.setText(icon.value)
            } then Actions.scaleTo(1f, 1f, 0.25f, Interpolation.swingOut)
        } else {
            accidentalLabel.setText(icon.value)
            Actions.delay(delay) then Actions.scaleTo(1f, 1f, 0.25f, Interpolation.swingOut)
        }
        queueActions(showAnim)
        currentIcon = icon
        return icon
    }

    override fun layout() {
        super.layout()
        setOrigin(width / 2, height / 2)
    }

    private fun initialize() {
        val self = this
        addListener(object : ClickListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                if (self.isDisabled) return
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
                if (self.isDisabled) return res
                self.addAction(Actions.scaleTo(Constants.PRESSED_SCALING, Constants.PRESSED_SCALING, .2f, Interpolation.exp10Out))
                return res
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                self.addAction(Actions.scaleTo(1f, 1f, .2f, Interpolation.exp10Out))
            }
        })
    }

    override fun setDisabled(isDisabled: Boolean) {
        _disabled = isDisabled
    }

    override fun isDisabled() = _disabled
}
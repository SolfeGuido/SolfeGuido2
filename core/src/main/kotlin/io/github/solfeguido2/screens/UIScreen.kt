package io.github.solfeguido2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FillViewport
import io.github.solfeguido2.actors.ButtonAnswerActor
import io.github.solfeguido2.actors.PianoAnswerActor
import io.github.solfeguido2.core.PreferencesManager
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.ui.PianoKey
import io.github.solfeguido2.ui.STextButton
import io.github.solfeguido2.ui.SlidingTable
import ktx.actors.onClick
import ktx.actors.onClickEvent
import ktx.actors.plusAssign
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.inject.Context
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actor

abstract class UIScreen(protected val context: Context) : KtxScreen, InputProcessor, GestureDetector.GestureListener {

    lateinit var rootActor: Actor
    private val slidingTables = mutableListOf<SlidingTable>()
    private val assetManager = context.inject<AssetStorage>()

    private val preferences: PreferencesManager = context.inject()

    val stage = stage(viewport = FillViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))

    private fun hasClickSound(actor: Actor?) =
        preferences.soundEnabled.volume > 0f && actor is Button && !actor.isDisabled && actor !is ButtonAnswerActor && actor !is PianoKey

    fun create(settings: StateParameter) {
        rootActor = setup(settings)
        rootActor.onClickEvent { evt ->
            val actor = evt.target
            if (hasClickSound(actor) || hasClickSound(actor?.parent) || hasClickSound(actor?.parent?.parent)) {
                assetManager.get<Sound>(Constants.CLICK_SOUND).play()
            }
        }
        stage += rootActor
        Gdx.input.inputProcessor = InputMultiplexer(GestureDetector(this), this)
    }

    protected abstract fun setup(settings: StateParameter): Actor

    protected open fun back(): Boolean = false

    fun <S> KWidget<S>.slidingTable(
        align: Int,
        skin: Skin = Scene2DSkin.defaultSkin,
        init: SlidingTable.(S) -> Unit = {}
    ): SlidingTable {
        val act = actor(SlidingTable(align, skin), init)
        slidingTables.add(act)
        return act
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.viewport.apply()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun keyDown(keycode: Int) = stage.keyDown(keycode)
    override fun keyTyped(character: Char) = stage.keyTyped(character)
    override fun keyUp(keycode: Int) =
        stage.keyUp(keycode) || (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) && back()

    override fun mouseMoved(screenX: Int, screenY: Int) = stage.mouseMoved(screenX, screenY)
    override fun scrolled(amountX: Float, amountY: Float) = stage.scrolled(amountX, amountY)
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) =
        stage.touchDown(screenX, screenY, pointer, button)

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = stage.touchDragged(screenX, screenY, pointer)
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) =
        stage.touchUp(screenX, screenY, pointer, button)

    // Gesture listener
    override fun tap(x: Float, y: Float, count: Int, button: Int) = false
    override fun longPress(x: Float, y: Float) = false
    override fun fling(velocityX: Float, velocityY: Float, button: Int) = false
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float) = false
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int) = false
    override fun zoom(initialDistance: Float, distance: Float) = false
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?) =
        false

    override fun pinchStop() {}
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int) = false
}
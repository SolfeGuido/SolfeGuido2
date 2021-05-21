package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FillViewport
import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.ui.SlidingTable
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actor
import kotlin.math.min

abstract class UIScreen(protected val context: Context) : KtxScreen, InputProcessor, GestureDetector.GestureListener {

    protected lateinit var stage: Stage
    private  lateinit var batch: SpriteBatch
    private val slidingTables = mutableListOf<SlidingTable>()

    override fun show() {
        super.show()
        batch = SpriteBatch()
        stage = stage(batch, FillViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
        Gdx.input.inputProcessor = InputMultiplexer(GestureDetector(this), this)
    }

     open fun create(settings: StateParameter)  {
        this.show()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stage.viewport.update(width, height)
    }

    fun <S> KWidget<S>.slidingTable(
            align: Int,
            skin: Skin = Scene2DSkin.defaultSkin,
            init: SlidingTable.(S) -> Unit = {}) : SlidingTable {
        val act =actor(SlidingTable(align, skin), init)
        slidingTables.add(act)
        return act
    }

    override fun render(delta: Float) {
        stage.act(min(delta, 1 / 30f))
        stage.draw()
    }

    override fun keyDown(keycode: Int)  = stage.keyDown(keycode)
    override fun keyTyped(character: Char) = stage.keyTyped(character)
    override fun keyUp(keycode: Int) = stage.keyUp(keycode)
    override fun mouseMoved(screenX: Int, screenY: Int) = stage.mouseMoved(screenX, screenY)
    override fun scrolled(amountX: Float, amountY: Float) = stage.scrolled(amountX, amountY)
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = stage.touchDown(screenX, screenY, pointer, button)
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = stage.touchDragged(screenX, screenY, pointer)
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = stage.touchUp(screenX, screenY, pointer, button)

    // Gesture listener
    override fun tap(x: Float, y: Float, count: Int, button: Int) = false
    override fun longPress(x: Float, y: Float) = false
    override fun fling(velocityX: Float, velocityY: Float, button: Int) = false
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float) = false
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int) = false
    override fun zoom(initialDistance: Float, distance: Float) = false
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?) = false
    override fun pinchStop() {}
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int) = false
}
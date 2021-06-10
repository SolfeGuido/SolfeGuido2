package io.github.solfeguido

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FillViewport
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.Jingles
import io.github.solfeguido.core.SoundHelper
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.factories.ParticlePool
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.screens.*
import io.github.solfeguido.skins.getPreloadSkin
import ktx.actors.stage
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import ktx.inject.Context
import ktx.inject.register
import ktx.scene2d.Scene2DSkin

class SolfeGuido : ApplicationListener {

    private lateinit var context: Context;
    private lateinit var stateMachine: StateMachine
    private lateinit var batch: SpriteBatch
    private lateinit var stage: Stage
    private val bgColor: Color by lazy { gCol("background") }

    override fun create() {
        KtxAsync.initiate()
        context = Context()
        Scene2DSkin.defaultSkin = getPreloadSkin()

        stateMachine = StateMachine(context)
            .addCurrentScreen<SplashScreen>()
            .addScreen<TransitionScreen>()
            .addScreen<MenuScreen>()
            .addScreen<GameCreationScreen>()
            .addScreen<LevelSelectionScreen>()
            .addScreen<OptionScreen>()
            .addScreen<PlayScreen>()
            .addScreen<ClassicSelectionScreen>()


        batch = SpriteBatch()
        stage = stage(batch, FillViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
        context.register {
            bindSingleton(Gdx.app.getPreferences(Constants.PREFERENCES_NAME))
            bindSingleton(ParticlePool(context))
            bindSingleton(Jingles(context))
            bindSingleton(AssetStorage(asyncContext = newAsyncContext(2)))
            bindSingleton(SoundHelper(context))
            bindSingleton(stateMachine)
            bindSingleton(batch)
            bindSingleton(stage)
        }

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.setCatchKey(Input.Keys.MENU, true)
    }

    override fun render() {
        val bg = bgColor
        Gdx.gl.glClearColor(bg.r, bg.b, bg.b, bg.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val delta = Gdx.graphics.deltaTime
        stage.act(delta)
        stage.draw()
        stateMachine.render(Gdx.graphics.deltaTime)
    }

    override fun pause() {
        stateMachine.pause()
    }

    override fun resume() {
        stateMachine.resume()
    }

    override fun resize(width: Int, height: Int) {
        stateMachine.resize(width, height)
    }

    override fun dispose() {
        stateMachine.dispose()

    }
}
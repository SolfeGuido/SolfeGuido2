package io.github.solfeguido

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.core.PreferencesManager
import io.github.solfeguido.core.*
import io.github.solfeguido.core.LevelManager
import io.github.solfeguido.factories.ParticlePool
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.screens.*
import io.github.solfeguido.skins.getPreloadSkin
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import ktx.inject.Context
import ktx.inject.register
import ktx.scene2d.Scene2DSkin

class SolfeGuido : ApplicationListener {

    private lateinit var context: Context;
    private lateinit var stateMachine: StateMachine
    private val bgColor: Color by lazy { gCol("background") }

    override fun create() {
        KtxAsync.initiate()
        context = Context()
        Scene2DSkin.defaultSkin = getPreloadSkin()

        stateMachine = StateMachine(context)
            .addCurrentScreen<SplashScreen>()
            .addScreen<TransitionScreen>()
            .addScreen<MenuScreen>()
            .addScreen<LevelSelectionScreen>()
            .addScreen<PlayScreen>()
            .addScreen<ClassicSelectionScreen>()
            .addScreen<OptionScreen>()

        val gamePreferences = Gdx.app.getPreferences(Constants.PREFERENCES_NAME)

        context.register {
            bindSingleton(gamePreferences)
            bindSingleton(PreferencesManager(gamePreferences))
            bindSingleton(ParticlePool(context))
            bindSingleton(Jingles(context))
            bindSingleton(AssetStorage(asyncContext = newAsyncContext(2)))
            bindSingleton(SoundHelper(context))
            bindSingleton(stateMachine)
            bindSingleton(StatsManager(gamePreferences))
            bindSingleton(LevelManager(gamePreferences))
        }

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.setCatchKey(Input.Keys.MENU, true)
    }

    override fun render() {
        val bg = bgColor
        Gdx.gl.glClearColor(bg.r, bg.b, bg.b, bg.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val delta = Gdx.graphics.deltaTime
        stateMachine.render(delta)
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
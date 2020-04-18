package io.github.solfeguido

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.Jingles
import io.github.solfeguido.core.SoundHelper
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.factories.ParticlePool
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.screens.NoteGuessScreen
import io.github.solfeguido.screens.MenuScreen
import io.github.solfeguido.screens.SplashScreen
import io.github.solfeguido.skins.getPreloadSkin
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import ktx.collections.gdxMapOf
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin

class SolfeGuido : ApplicationListener {

    private lateinit var context: Context;
    private lateinit var stateMachine: StateMachine

    override fun create() {
        KtxAsync.initiate()
        context = Context()
        Scene2DSkin.defaultSkin = getPreloadSkin()

        stateMachine = StateMachine(gdxMapOf(
                MenuScreen::class.java to { MenuScreen(context) },
                SplashScreen::class.java to { SplashScreen(context) },
                NoteGuessScreen::class.java to { NoteGuessScreen(context) }
        ), SplashScreen::class.java)
        context.register {
            bindSingleton(Gdx.app.getPreferences(Constants.PREFERENCES_NAME))
            bindSingleton(ParticlePool(context))
            bindSingleton(Jingles(context))
            bindSingleton(AssetStorage(asyncContext = newAsyncContext(2)))
            bindSingleton(SoundHelper(context))
            bindSingleton(stateMachine)
        }
    }

    override fun render() {
        val bg = gCol("background")
        Gdx.gl.glClearColor(bg.r, bg.b, bg.b, bg.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
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
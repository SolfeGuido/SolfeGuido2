package io.github.solfeguido2

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import io.github.solfeguido2.core.*
import io.github.solfeguido2.factories.ParticlePool
import io.github.solfeguido2.factories.gCol
import io.github.solfeguido2.screens.*
import io.github.solfeguido2.settings.gamemode.IGameModeOptions
import io.github.solfeguido2.settings.gamemode.LevelOptions
import io.github.solfeguido2.settings.gamemode.NoteGuessOptions
import io.github.solfeguido2.settings.generator.IGeneratorOptions
import io.github.solfeguido2.settings.generator.MidiGenerator
import io.github.solfeguido2.settings.generator.RandomGenerator
import io.github.solfeguido2.skins.getPreloadSkin
import io.github.solfeguido2.structures.Constants
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import ktx.inject.Context
import ktx.inject.register
import ktx.scene2d.Scene2DSkin

class SolfeGuido : ApplicationListener {

    private lateinit var context: Context;
    private lateinit var stateMachine: StateMachine
    private val bgColor
        get() = gCol("background")

    override fun create() {
        KtxAsync.initiate()
        context = Context()
        val gamePreferences = Gdx.app.getPreferences(Constants.PREFERENCES_NAME)
        val prefManager = PreferencesManager(gamePreferences)
        Scene2DSkin.defaultSkin = getPreloadSkin(prefManager.theme)

        val jsonSerializer = Json {
            serializersModule = SerializersModule {
                polymorphic(IGameModeOptions::class) {
                    subclass(NoteGuessOptions::class)
                    subclass(LevelOptions::class)
                }

                polymorphic(IGeneratorOptions::class) {
                    subclass(RandomGenerator::class)
                    subclass(MidiGenerator::class)
                }
            }

        }

        stateMachine = StateMachine(context)
            .addCurrentScreen<SplashScreen>()
            .addScreen<TransitionScreen>()
            .addScreen<MenuScreen>()
            .addScreen<LevelSelectionScreen>()
            .addScreen<PlayScreen>()
            .addScreen<ClassicSelectionScreen>()
            .addScreen<OptionScreen>()
            .addScreen<CreditsScreen>()
            .addScreen<StatsScreen>()


        val assetStorage = AssetStorage(asyncContext = newAsyncContext(Constants.ASYNC_THREADS))
        context.register {
            bindSingleton(gamePreferences)
            bindSingleton(prefManager)
            bindSingleton(ParticlePool(context))
            bindSingleton(Jingles(context))
            bindSingleton(assetStorage)
            bindSingleton(SoundManager(assetStorage))
            bindSingleton(stateMachine)
            bindSingleton(StatsManager(gamePreferences, jsonSerializer))
            bindSingleton(LevelManager(gamePreferences))
        }

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.setCatchKey(Input.Keys.MENU, true)
    }

    override fun render() {
        val bg = bgColor
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a)
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
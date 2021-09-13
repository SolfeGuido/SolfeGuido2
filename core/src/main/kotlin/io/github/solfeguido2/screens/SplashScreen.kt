package io.github.solfeguido2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.I18NBundleLoader
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.utils.I18NBundle
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.core.*
import io.github.solfeguido2.core.LevelManager
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.enums.Nls
import io.github.solfeguido2.loaders.FontLoader
import io.github.solfeguido2.loaders.MidiLoader
import io.github.solfeguido2.midi.MidiFile
import io.github.solfeguido2.settings.GameSettings
import io.github.solfeguido2.skins.getDefaultSkin
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.assets.async.toIdentifier
import ktx.async.KtxAsync
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.*
import java.util.*


class SplashScreen(context: Context) : UIScreen(context) {

    private var assetManager: AssetStorage = context.inject()
    private lateinit var progressLabel: Label
    private lateinit var pBar: ProgressBar

    private var toLoad = 0

    override fun setup(settings: StateParameter): Actor {
        toLoad = 10
        val jingles: Jingles = context.inject()
        val start = System.currentTimeMillis()

        KtxAsync.launch {
            val soundManager: SoundManager = context.inject()
            val stats: StatsManager = context.inject()
            val levels: LevelManager = context.inject()

            stats.loadSave()
            levels.load()

            toLoad += jingles.allJingles.size + soundManager.existingSounds.size

            // Set midi file loader
            assetManager.setLoader(".mid") { MidiLoader(assetManager.fileResolver) }

            // Freetype font loader
            FreeTypeFontGenerator.setMaxTextureSize(2048)
            assetManager.setLoader<BitmapFont>(".woff") { FontLoader(assetManager.fileResolver) }

            //Load
            assetManager.apply {
                load<Texture>(Constants.PARTICLE_IMAGE)
                load<Texture>(Constants.APP_ICON)
                load<TextureAtlas>(Constants.FLAGS_ATLAS)



                jingles.allJingles.map { loadAsync<MidiFile>(it.path()) }.toTypedArray()


                soundManager.existingSounds.map {
                    load<Sound>(SoundManager.toAssetName(it.key.midiIndex))
                }.toTypedArray()

                load<Sound>(Constants.CLICK_SOUND)


                load("bigIcon.woff", FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.ICONS_PATH
                    it.fontParameters.apply {
                        size = Constants.BIG_ICON_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                        characters = IconName.values().joinToString("") { icon -> icon.value }
                    }
                })
                load(Constants.PRIMARY_FONT, FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.PRIMARY_FONT
                    it.fontParameters.apply {
                        size = Constants.TITLE_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                    }
                })
                load("icon.woff", FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.ICONS_PATH
                    it.fontParameters.apply {
                        size = Constants.TITLE_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                    }
                })
                load(Constants.TITLE_FONT, FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.TITLE_FONT
                    it.fontParameters.apply {
                        size = Constants.TITLE_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                    }
                })
            }
        }.invokeOnCompletion {
            it?.let {
                ktx.log.error(it, "FATAL") { "Failed to load game assets" }
                Gdx.app.exit()
            } ?: kotlin.run {
                val end = System.currentTimeMillis()
                info("START") { "Assets loaded in ${end - start}ms" }
                val preferences: PreferencesManager = context.inject()

                val baseFileHandle = Gdx.files.internal(Constants.TRANSLATIONS_PATH)
                Nls.i18nBundle = I18NBundle.createBundle(baseFileHandle, Locale(preferences.language.code));

                Scene2DSkin.defaultSkin = getDefaultSkin(
                    assetManager,
                    context.inject<PreferencesManager>().theme
                )
                //jingles.registerJingles(assetManager)
                context.inject<StateMachine>().switch<MenuScreen>()
            }
        }

        return scene2d.table {
            setFillParent(true)
            progressLabel = label("0%")
            row()
            pBar = progressBar {
                this.setRange(0f, 100f)
                this.setAnimateDuration(0.1f)
                it.expandX().fillX().pad(5f, 40f, 0f, 40f)
            }
        }

    }

    override fun render(delta: Float) {
        super.render(delta)
        val prog = assetManager.progress
        if (prog.isFailed) {
            Gdx.app.log("FATAL", "Failed to load games assets")
            Gdx.app.exit()
        } else {
            val progress = ((prog.loaded * 100f) / toLoad)
            progressLabel.setText("${progress.toInt()}%")
            pBar.value = progress
        }
    }
}
package io.github.solfeguido.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.Jingles
import io.github.solfeguido.core.SoundHelper
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.loaders.FontLoader
import io.github.solfeguido.midi.MidiFile
import io.github.solfeguido.loaders.MidiLoader
import io.github.solfeguido.skins.getDefaultSkin
import kotlinx.coroutines.launch
import ktx.actors.plusAssign
import ktx.assets.async.AssetStorage
import ktx.inject.Context
import ktx.async.KtxAsync
import ktx.scene2d.*


class SplashScreen(context: Context) : UIScreen(context) {

    private var assetManager: AssetStorage = context.inject()
    private lateinit var progressLabel: Label
    private lateinit var pBar: ProgressBar

    private var toLoad = 0

    override fun show() {
        super.show()
        stage += scene2d.table {
            setFillParent(true)
            progressLabel = label("0%")
            row()
            pBar = progressBar {
                this.setRange(0f, 100f)
                this.setAnimateDuration(0.1f)
                it.expandX().fillX()
            }
        }

        toLoad = 7
        val jingles: Jingles = context.inject()


        KtxAsync.launch {
            val soundHelper: SoundHelper = context.inject()
            toLoad += jingles.allJingles.size + soundHelper.existingSounds.size

            // Set midi file loader
            assetManager.setLoader(".mid") { MidiLoader(assetManager.fileResolver) }

            // Freetype font loader
            FreeTypeFontGenerator.setMaxTextureSize(2048)
            assetManager.setLoader<BitmapFont>(".woff") { FontLoader(assetManager.fileResolver) }

            //Load
            assetManager.apply {
                load<Texture>("images/particle.png")
                load<Texture>("images/appIcon.png")


                jingles.allJingles.map { loadAsync<MidiFile>(it.path()) }.toTypedArray()


                soundHelper.existingSounds.map {
                    load<Sound>("sounds/notes/${it.key}.mp3")
                }.toTypedArray()

                load<Sound>(Constants.CLICK_SOUND)


                load("bigIcon.woff", FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.ICONS_PATH
                    it.fontParameters.apply {
                        size = (Gdx.graphics.height / 2.7f).toInt()
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                        characters = IconName.values().joinToString("") { icon -> icon.value }
                    }
                })
                load<BitmapFont>(Constants.PRIMARY_FONT, FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.PRIMARY_FONT
                    it.fontParameters.apply {
                        size = Constants.TITLE_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                    }
                })
                load<BitmapFont>("icon.woff", FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.ICONS_PATH
                    it.fontParameters.apply {
                        size = Constants.TITLE_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                    }
                })
                load<BitmapFont>(Constants.TITLE_FONT, FontLoader.FontLoaderParameter().also {
                    it.fontFileName = Constants.TITLE_FONT
                    it.fontParameters.apply {
                        size = Constants.TITLE_SIZE
                        minFilter = Texture.TextureFilter.Linear
                        magFilter = Texture.TextureFilter.Linear
                    }
                })
            }

            Scene2DSkin.defaultSkin = getDefaultSkin(assetManager)
            jingles.registerJingles(assetManager)
            jingles.playJingle("Startup")
            context.inject<StateMachine>().switch<MenuScreen>()
        }
    }

    override fun render(delta: Float) {
        super.render(delta)
        val prog = assetManager.progress
        if (prog.isFailed) {
            Gdx.app.exit()
        } else {
            val progress = (  (prog.loaded * 100f) / toLoad)
            progressLabel.setText("${progress.toInt()}%")
            pBar.value = progress
        }
    }
}
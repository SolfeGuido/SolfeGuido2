package io.github.solfeguido.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import io.github.solfeguido.config.Constants
import io.github.solfeguido.core.Jingles
import io.github.solfeguido.core.SoundHelper
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.midi.MidiFile
import io.github.solfeguido.skins.getDefaultSkin
import io.github.solfeguido.ui.UIScreen
import io.github.solfeguido.utils.MidiLoader
import ktx.actors.plusAssign
import ktx.assets.load
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.assets.setLoader
import ktx.scene2d.progressBar


class SplashScreen(context: Context) : UIScreen(context) {

    private var totalAssets : Int = 0
    private var assetManager : AssetManager = context.inject()
    private lateinit var progressLabel: Label
    private lateinit var pBar : ProgressBar

    override fun show() {
        super.show()
        stage += table {
            setFillParent(true)
            progressLabel = label("0%")
            row()
            pBar = progressBar {
                this.setRange(0f, 100f)
                this.setAnimateDuration(0.1f)
            }
            pBar.inCell.expandX().fillX()
        }

        val soundHelper: SoundHelper = context.inject()
        val jingles: Jingles = context.inject()

        assetManager.setLoader<MidiFile, MidiLoader.MidiLoaderParameter>(MidiLoader(InternalFileHandleResolver()))
        jingles.allJingles.forEach {
            assetManager.load<MidiFile>(it.path())
        }


        soundHelper.existingSounds.forEach {
            assetManager.load<Sound>("sounds/notes/${it.key}.mp3")
        }
        assetManager.load<Sound>(Constants.CLICK_SOUND)


        FreeTypeFontGenerator.setMaxTextureSize(2048)
        assetManager.registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)

        assetManager.load("bigIcon.ttf", FreetypeFontLoader.FreeTypeFontLoaderParameter().also {

            it.fontFileName = Constants.ICONS_PATH
            with(it.fontParameters){
                size = Gdx.graphics.height / 2
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.Linear
                characters = IconName.values().joinToString("") { icon -> icon.value }
            }
        })

        assetManager.loadFreeTypeFont(Constants.PRIMARY_FONT) {
            size = Constants.TITLE_SIZE
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }

        assetManager.load("icon.ttf", FreetypeFontLoader.FreeTypeFontLoaderParameter().also {
            it.fontFileName = Constants.ICONS_PATH
            it.fontParameters.size = Constants.TITLE_SIZE
            it.fontParameters.minFilter = Texture.TextureFilter.Linear
            it.fontParameters.magFilter = Texture.TextureFilter.Linear
        })

        assetManager.loadFreeTypeFont(Constants.TITLE_FONT) {
            size = Constants.TITLE_SIZE
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }


        totalAssets = assetManager.queuedAssets
    }

    override fun render(delta: Float) {
        super.render(delta)
        if(assetManager.update()){
            assetManager.finishLoading()
            Scene2DSkin.defaultSkin = getDefaultSkin(context.inject())
            val jingles : Jingles = context.inject()
            jingles.registerJingles(assetManager)
            jingles.playJingle("Startup")
            context.inject<StateMachine>().switch<MenuScreen>()
        }
        val progress = (( (totalAssets - assetManager.queuedAssets) / totalAssets.toFloat()) * 100).toInt()
        progressLabel.setText("$progress%")
        pBar.value = progress.toFloat()
    }
}
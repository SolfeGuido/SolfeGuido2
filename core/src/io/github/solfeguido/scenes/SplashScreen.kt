package io.github.solfeguido.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Json
import io.github.solfeguido.Constants
import io.github.solfeguido.core.Jingle
import io.github.solfeguido.core.Jingles
import io.github.solfeguido.core.SoundHelper
import io.github.solfeguido.core.StateMachine
import io.github.solfeguido.core.music.NoteNameEnum
import io.github.solfeguido.skins.getDefaultSkin
import io.github.solfeguido.ui.UIScreen
import io.github.solfeguido.utils.JingleLoader
import io.github.solfeguido.utils.NoteDataPool
import ktx.actors.plusAssign
import ktx.assets.setLoader
import ktx.assets.load
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.table


class SplashScreen(context: Context) : UIScreen(context) {

    private var totalAssets : Int = 0
    private var assetManager : AssetManager = context.inject()
    private lateinit var progressLabel: Label

    override fun show() {
        super.show()
        info { "Showing" }
        stage += table {
            setFillParent(true)
            progressLabel = label("0%")
        }

        val soundHelper: SoundHelper = context.inject()
        val jingles: Jingles = context.inject()


        assetManager.setLoader<Jingle, JingleLoader.JingleParameter>(JingleLoader(InternalFileHandleResolver()))
        val params = JingleLoader.JingleParameter(Json())

        jingles.allJingles.forEach {
            assetManager.load<Jingle>(it.path(), params)
        }

        soundHelper.existingSounds.forEach {
            assetManager.load<Sound>("sounds/notes/$it.mp3")
        }
        assetManager.load<Sound>("sounds/click.wav")

        assetManager.registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
        assetManager.loadFreeTypeFont("fonts/Oswald.ttf") {
            size = Constants.TITLE_SIZE
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }
        assetManager.loadFreeTypeFont("fonts/Icons.ttf") {
            size = Constants.TITLE_SIZE
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }
        assetManager.loadFreeTypeFont("fonts/MarckScript.ttf") {
            size = Constants.TITLE_SIZE
            minFilter = Texture.TextureFilter.Linear
            magFilter = Texture.TextureFilter.Linear
        }


        totalAssets = assetManager.queuedAssets
    }

    override fun render(delta: Float) {
        super.render(delta)
        if(assetManager.update()){
            Scene2DSkin.defaultSkin = getDefaultSkin(context.inject())
            val jingles : Jingles = context.inject()
            jingles.registerJingles(assetManager)
            jingles.playJingle("startup")
            context.inject<StateMachine>().switch<MenuScreen>()
        }
        val progress = (( (totalAssets - assetManager.queuedAssets) / totalAssets.toFloat()) * 100).toInt()
        progressLabel.setText("$progress%")
    }
}
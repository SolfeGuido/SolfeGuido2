package io.github.solfeguido.skins

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.solfeguido.config.Constants
import ktx.assets.async.AssetStorage
import ktx.style.*

fun getDefaultSkin(assetManager: AssetStorage): Skin{
    return skin {
        val self = this
        color("font", 68f/255f, 68f/255f, 68f/255f,  1f)
        color("fontHover", 0.8f, 0.8f, 0.8f, 1f)
        color("correct", 125 / 255f,220 / 255f,31 / 255f, 1f)
        color("error", 179 / 255f,40 / 255f,77 / 255f, 1f)
        label {
            font = assetManager.get<BitmapFont>(Constants.TITLE_FONT)
            fontColor = Color(self.getColor("font"))
        }
        label(name = "iconStyle") {
            font = assetManager.get<BitmapFont>("icon.woff")
            font.data.setScale(.9f)
        }
        label(name = "bigIconStyle") {
            font = assetManager.get<BitmapFont>("bigIcon.woff")
        }
        textButton( name=  "iconButtonStyle") {
            font = assetManager.get<BitmapFont>("icon.woff")
            fontColor = self.getColor("font")
        }
    }
}
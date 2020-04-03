package io.github.solfeguido.skins

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.freetype.loadFreeTypeFont
import ktx.style.*

fun getDefaultSkin(assetManager: AssetManager): Skin{
    return skin {
        val self = this
        color("font", 68f/255f, 68f/255f, 68f/255f,  1f)
        color("fontHover", 0.8f, 0.8f, 0.8f, 1f)
        label {
            font = assetManager.get<BitmapFont>("fonts/MarckScript.ttf")
            fontColor = Color(self.getColor("font"))
        }
        label(name = "iconStyle") {
            font = assetManager.get<BitmapFont>("icon.ttf")
            font.data.setScale(.9f)
        }
        label(name = "bigIconStyle") {
            font = assetManager.get<BitmapFont>("bigIcon.ttf")
        }
        textButton( name=  "iconButtonStyle") {
            font = assetManager.get<BitmapFont>("icon.ttf")
            fontColor = self.getColor("font")
        }
    }
}
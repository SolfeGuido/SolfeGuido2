package io.github.solfeguido.skins

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.solfeguido.config.Constants
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.borderColorDrawable
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.assets.async.AssetStorage
import ktx.scene2d.dialog
import ktx.style.*

fun copyFont(font: BitmapFont) = BitmapFont(font.data, font.region, font.usesIntegerPositions())

fun getDefaultSkin(assetManager: AssetStorage): Skin{
    val primaryFont: BitmapFont = assetManager[Constants.PRIMARY_FONT]
    val titleFont : BitmapFont = assetManager[Constants.TITLE_FONT]
    val iconFont: BitmapFont = assetManager["icon.woff"]
    val bigIconFont: BitmapFont = assetManager["bigIcon.woff"]

    return skin {
        val self = this
        color("background", 245 / 255f,245 / 255f,245 / 255f)
        color("font", 68f/255f, 68f/255f, 68f/255f,  1f)
        color("fontHover", 0.8f, 0.8f, 0.8f, 1f)
        color("correct", 125 / 255f,220 / 255f,31 / 255f, 1f)
        color("error", 179 / 255f,40 / 255f,77 / 255f, 1f)
        label {
            font = titleFont
            fontColor = gCol("font")
        }

        label(name = "contentLabelStyle") {
            font = copyFont(primaryFont)
            font.data.setScale(0.7f)
            fontColor = gCol("font")
        }


        label(name = "iconStyle") {
            font = iconFont
        }

        label(name = "smallIconStyle") {
            font = primaryFont
        }

        label(name = "bigIconStyle") {
            font = bigIconFont
        }

        borderButton( name=  "iconButtonStyle") {
            labelStyle = "iconStyle"
            borderThickness = 0f
            borderColor = gCol("background")
        }

        borderButton(name = "iconBorderButtonStyle") {
            labelStyle = "iconStyle"
            borderThickness = 1.3f
            borderColor = gCol("font")
        }

        borderButton {
            labelStyle = "contentLabelStyle"
            borderThickness = 1.3f
            borderColor = gCol("font")
        }

        textButton(name = "blackPianoKey")  {
            font = primaryFont
            fontColor = self.getColor("background")
        }
        textButton(name = "whitePianoKey") {
            font = primaryFont
            fontColor = self.getColor("font")
        }

        borderButton(name= "borderButtonStyle") {
            labelStyle = "contentLabelStyle"
            fontColor = self.getColor("font")
            borderThickness = 1.3f
            borderColor = gCol("font")
        }

        //TODO : for border style:  border thickness, and border color
        label(name = "borderButtonTopIconStyle") {
            font = iconFont
            fontColor = self.getColor("font")
        }

        window {
            this.titleFont = primaryFont
            titleFontColor = gCol("font")
            background = colorDrawable(gCol("background"))

            //stageBackground = colorDrawable(Color(0f , 0f, 0f , 0.7f))
        }

        textButton {
            fontColor = gCol("font")
            font = primaryFont
        }

        scrollPane {
            background = colorDrawable(TRANSPARENT)
            vScroll = colorDrawable(self.getColor("fontHover"), 10)
            vScrollKnob = colorDrawable(self.getColor("font"),10)
        }

    }
}
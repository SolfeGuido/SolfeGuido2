package io.github.solfeguido.skins

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.solfeguido.enums.Theme
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import ktx.assets.async.AssetStorage
import ktx.style.*

fun copyFont(font: BitmapFont) = BitmapFont(font.data, font.region, font.usesIntegerPositions())

fun getDefaultSkin(assetManager: AssetStorage, theme: Theme): Skin {
    val primaryFont: BitmapFont = assetManager[Constants.PRIMARY_FONT]
    val titleFont: BitmapFont = assetManager[Constants.TITLE_FONT]
    val iconFont: BitmapFont = assetManager["icon.woff"]
    val bigIconFont: BitmapFont = assetManager["bigIcon.woff"]

    return skin {
        val smallPrimary = copyFont(primaryFont)
        val self = this
        if (theme == Theme.Light) {
            color("background", 245 / 255f, 245 / 255f, 245 / 255f)
            color("font", 68f / 255f, 68f / 255f, 68f / 255f, 1f)
            color("fontHover", 0.8f, 0.8f, 0.8f, 1f)
            color("correct", 125 / 255f, 220 / 255f, 31 / 255f, 1f)
            color("error", 179 / 255f, 40 / 255f, 77 / 255f, 1f)
        } else {
            color("background", 34f / 255f, 40f / 255f, 49f / 255f, 1f)
            color("font", 238f / 255f, 238f / 255f, 238f / 255f, 1f)
            color("fontHover", 45f / 255f, 50f / 255f, 59f / 255f, 1f)
            color("correct", 72f / 255f, 158f / 255f, 115f / 255f, 1f)
            color("error", 168f / 255f, 17f / 255f, 0f, 1f)
        }

        label {
            font = titleFont
            fontColor = gCol("font")
        }

        label(name = "contentLabelStyle") {
            font = smallPrimary
            fontColor = gCol("font")
        }

        label(name = "noteNameStyle") {
            font = copyFont(primaryFont)
            font.data.setScale(1.25f)
            fontColor = self.getColor("error")

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

        borderButton(name = "iconButtonStyle") {
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
            //disabledColor = gCol("fontHover")
        }

        textButton(name = "blackPianoKey") {
            font = primaryFont
            fontColor = self.getColor("background")
        }
        textButton(name = "whitePianoKey") {
            font = primaryFont
            fontColor = self.getColor("font")
        }

        borderButton(name = "borderButtonStyle") {
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
            vScroll = colorDrawable(TRANSPARENT)// colorDrawable(self.getColor("fontHover"), 10)
            vScrollKnob = colorDrawable(TRANSPARENT)// colorDrawable(self.getColor("font"),10)
        }

        checkBox {
            font = smallPrimary
            up = colorDrawable(gCol("background"))
            fontColor = gCol("font")
            checkedFontColor = Color.BLUE
        }

        checkBox("icon") {
            font = iconFont
            fontColor = gCol("font")
            checkedFontColor = Color.BLUE
        }

    }
}
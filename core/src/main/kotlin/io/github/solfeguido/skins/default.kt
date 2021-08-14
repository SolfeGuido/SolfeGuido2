package io.github.solfeguido.skins

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.solfeguido.enums.Language
import io.github.solfeguido.enums.Theme
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.factories.TRANSPARENT
import io.github.solfeguido.factories.colorDrawable
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
            color("stripe", 151 / 255f, 155 / 255f, 194 / 255f, 74 / 255f)
        } else {
            color("background", 34f / 255f, 40f / 255f, 49f / 255f, 1f)
            color("font", 238f / 255f, 238f / 255f, 238f / 255f, 1f)
            color("fontHover", 45f / 255f, 50f / 255f, 59f / 255f, 1f)
            color("correct", 72f / 255f, 158f / 255f, 115f / 255f, 1f)
            color("error", 168f / 255f, 17f / 255f, 0f, 1f)
            color("stripe", 70 / 255f, 77 / 255f, 87 / 255f, 74 / 255f)
        }

        label {
            font = titleFont
            fontColor = self.getColor("font")
        }

        label(name = "contentLabelStyle") {
            font = smallPrimary
            fontColor = self.getColor("font")
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
            borderColor = self.getColor("background")
        }

        borderButton(name = "iconBorderButtonStyle") {
            labelStyle = "iconStyle"
            borderThickness = 1.3f
            borderColor = self.getColor("font")
        }

        borderButton {
            labelStyle = "contentLabelStyle"
            borderThickness = 1.3f
            borderColor = self.getColor("font")
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
            borderColor = self.getColor("font")
        }

        //TODO : for border style:  border thickness, and border color
        label(name = "borderButtonTopIconStyle") {
            font = iconFont
            fontColor = self.getColor("font")
        }

        window {
            this.titleFont = primaryFont
            titleFontColor = self.getColor("font")
            background = colorDrawable(self.getColor("background"))

            //stageBackground = colorDrawable(Color(0f , 0f, 0f , 0.7f))
        }

        textButton {
            fontColor = self.getColor("font")
            font = primaryFont
        }

        scrollPane {
            background = colorDrawable(TRANSPARENT)
            vScroll = colorDrawable(TRANSPARENT)// colorDrawable(self.getColor("fontHover"), 10)
            vScrollKnob = colorDrawable(TRANSPARENT)// colorDrawable(self.getColor("font"),10)
        }

        checkBox {
            font = smallPrimary
            up = colorDrawable(self.getColor("background"))
            fontColor = self.getColor("font")
            checkedFontColor = self.getColor("correct")
        }

        checkBox("icon") {
            font = iconFont
            fontColor = self.getColor("font")
            checkedFontColor = self.getColor("correct")
        }

        val flagsTextureAtlas = assetManager.get<TextureAtlas>(Constants.FLAGS_ATLAS)
        Language.values().forEach {
            checkBox(it.code) {
                font = smallPrimary
                up = TextureRegionDrawable(flagsTextureAtlas.findRegion(it.code))
            }
        }

    }
}
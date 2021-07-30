package io.github.solfeguido.skins

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.solfeguido.enums.Theme
import ktx.style.label
import ktx.style.progressBar
import ktx.style.skin
import io.github.solfeguido.factories.colorDrawable
import ktx.style.color


fun getPreloadSkin(theme: Theme): Skin {
    return skin {
        if (theme == Theme.Light) {
            color("background", 245 / 255f, 245 / 255f, 245 / 255f)
            color("font", 68f / 255f, 68f / 255f, 68f / 255f, 1f)
        } else {
            color("background", 34f / 255f, 40f / 255f, 49f / 255f)
            color("font", 238f / 255f, 238f / 255f, 238f / 255f, 1f)
        }
        val self = this

        progressBar("default-horizontal") {
            background = colorDrawable(self.getColor("background"))
            knob = colorDrawable(self.getColor("font"))
            knobBefore = colorDrawable(self.getColor("font"))
        }

        label {
            font = BitmapFont()
            fontColor = self.getColor("font")
        }
    }
}
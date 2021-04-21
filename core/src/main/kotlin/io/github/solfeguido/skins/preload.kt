package io.github.solfeguido.skins

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.style.label
import ktx.style.progressBar
import ktx.style.skin
import io.github.solfeguido.factories.colorDrawable
import ktx.style.color


const val PROGRESS_BAR_HEIGHT = 2

fun getPreloadSkin() : Skin {
    return skin {
        label {
            font = BitmapFont()
            fontColor = Color.BLACK
        }
        color("background", 245 / 255f,245 / 255f,245 / 255f)
        color("font", 68f/255f, 68f/255f, 68f/255f,  1f)
        val self = this

        progressBar("default-horizontal") {
            background = colorDrawable(self.getColor("background") )
            knob = colorDrawable(self.getColor("font") )
            knobBefore = colorDrawable(Color(0.1f, 0.1f, 0.1f, 1f))
        }
    }
}
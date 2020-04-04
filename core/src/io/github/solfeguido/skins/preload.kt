package io.github.solfeguido.skins

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.style.label
import ktx.style.progressBar
import ktx.style.skin
import io.github.solfeguido.factories.colorDrawable


const val PROGRESS_BAR_HEIGHT = 2

fun getPreloadSkin() : Skin {
    return skin {
        label {
            font = BitmapFont()
            fontColor = Color.BLACK
        }

        progressBar("default-horizontal") {
            background = colorDrawable(1, PROGRESS_BAR_HEIGHT, Color.WHITE)
            knob = colorDrawable(10, PROGRESS_BAR_HEIGHT, Color.BLACK)
            knobBefore = colorDrawable(10, PROGRESS_BAR_HEIGHT, Color(0.1f, 0.1f, 0.1f, 1f))
        }
    }
}
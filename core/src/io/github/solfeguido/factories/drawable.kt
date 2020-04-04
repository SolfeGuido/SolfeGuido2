package io.github.solfeguido.factories

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.scene2d.Scene2DSkin

fun colorDrawable(width: Int, height: Int, color: Color): Drawable {
    val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fill()

    val drawable = TextureRegionDrawable(TextureRegion(Texture(pixmap)))
    pixmap.dispose()
    return drawable
}

fun gCol(name: String) = Color(Scene2DSkin.defaultSkin.getColor(name))

val TRANSPARENT = Color(1f, 1f, 1f, 0f)
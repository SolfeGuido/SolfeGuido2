package io.github.solfeguido2.factories

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.scene2d.Scene2DSkin

fun colorDrawable(color: Color, width: Int = 1, height: Int = 1) = Pixmap(width, height, Pixmap.Format.RGBA8888).let {
        it.setColor(color)
        it.fill()
        TextureRegionDrawable(TextureRegion(Texture(it))).also { _ -> it.dispose() }
}

fun borderColorDrawable(width : Int, height: Int, borderWidth: Int, borderColor: Color, bgColor: Color) = Pixmap(width, height, Pixmap.Format.RGBA8888).let {
    it.setColor(borderColor)
    it.fill()
    it.setColor(bgColor)
    it.fillRectangle(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2))
    TextureRegionDrawable(TextureRegion(Texture(it))).also { _ -> it.dispose()  }
}

fun gCol(name: String) = Color(Scene2DSkin.defaultSkin.getColor(name))

val TRANSPARENT = Color(1f, 1f, 1f, 0f)
package io.github.solfeguido.factories

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import io.github.solfeguido.config.Constants

object Shaders {

    private const val DEFAULT_VERTEX_PATH = "${Constants.SHADERS_PATH}/vertex.glsl"
    private const val NOTE_FADE_PATH = "${Constants.SHADERS_PATH}/noteFade.glsl"

    val NoteFade by lazy {
        val res = ShaderProgram(
            Gdx.files.internal(DEFAULT_VERTEX_PATH),
            Gdx.files.internal(NOTE_FADE_PATH)
        )
        if (!res.isCompiled)
            throw Error("Failed to compile noteFade Shader : ${res.log}")
        res
    }
}
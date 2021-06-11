package io.github.solfeguido.factories

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import io.github.solfeguido.config.Constants

object Shaders {

    private const val DEFAULT_VERTEX_PATH = "${Constants.SHADERS_PATH}/vertex.glsl"
    private const val NOTE_FADE_PATH = "${Constants.SHADERS_PATH}/noteFade.glsl"

    val NoteFade by lazy {
        ShaderProgram(
            Gdx.files.internal(DEFAULT_VERTEX_PATH),
            Gdx.files.internal(NOTE_FADE_PATH)
        ).also {
            it.setAttributef("a_viewport_size", Constants.WIDTH.toFloat(), Constants.HEIGHT.toFloat(), 0f, 0f)
            if (!it.isCompiled)
                throw Error("Failed to compile noteFade Shader : ${it.log}")
        }
    }
}
package io.github.solfeguido.settings.generator

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

interface IGeneratorOptions : Json.Serializable {
    override fun read(json: Json, jsonData: JsonValue) {}

    override fun write(json: Json) {}
}
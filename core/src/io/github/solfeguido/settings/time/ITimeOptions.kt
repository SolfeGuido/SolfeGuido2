package io.github.solfeguido.settings.time

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

interface ITimeOptions : Json.Serializable {
    override fun read(json: Json, jsonData: JsonValue) {}

    override fun write(json: Json) {}
}
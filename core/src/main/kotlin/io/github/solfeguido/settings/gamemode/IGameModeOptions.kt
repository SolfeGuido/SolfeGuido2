package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue

interface IGameModeOptions : Json.Serializable {

    override fun read(json: Json, jsonData: JsonValue) {}

    override fun write(json: Json) {}
}
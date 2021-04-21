package io.github.solfeguido.settings.generator

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.readValue

class RangedRandomOptions : IGeneratorOptions {
    var minNote = 60
    var maxNote = 60

    override fun read(json: Json, jsonData: JsonValue) {
        minNote = json.readValue( jsonData, "minNote")
        maxNote = json.readValue(jsonData, "maNote")
    }

    override fun write(json: Json) {
        json.writeValue("minNote", minNote)
        json.writeValue("maxNote", maxNote)
    }

}
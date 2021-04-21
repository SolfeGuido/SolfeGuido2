package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.ClefEnum
import ktx.json.readValue

class MeasureSettings : Json.Serializable {

    var clef: ClefEnum = ClefEnum.GClef
    var minNote = 60
    var maxNote = 60
    var canHaveAccidentals = false


    override fun write(json: Json) {
        json.writeValue("clef", clef)
        json.writeValue("minNote", minNote)
        json.writeValue("maxNote", maxNote)
        json.writeValue("canHaveAccidentals", canHaveAccidentals)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        clef = json.readValue(jsonData, "clef")
        minNote = json.readValue(jsonData, "minNote")
        maxNote = json.readValue(jsonData, "maxNote")
        canHaveAccidentals = json.readValue(jsonData, "canHaveAccidentals")
    }

}

package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import ktx.json.readValue

class MeasureSettings(
    var clef: ClefEnum = ClefEnum.GClef,
    var signature: KeySignatureEnum = KeySignatureEnum.CMajor,
    var minNote: Int = 60,
    var maxNote: Int = 60,
    var canHaveAccidentals: Boolean = false
) : Json.Serializable {

    override fun write(json: Json) {
        json.writeValue("clef", clef)
        json.writeValue("signature", signature)
        json.writeValue("minNote", minNote)
        json.writeValue("maxNote", maxNote)
        json.writeValue("canHaveAccidentals", canHaveAccidentals)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        clef = json.readValue(jsonData, "clef")
        signature = json.readValue(jsonData, "signature")
        minNote = json.readValue(jsonData, "minNote")
        maxNote = json.readValue(jsonData, "maxNote")
        canHaveAccidentals = json.readValue(jsonData, "canHaveAccidentals")
    }

}

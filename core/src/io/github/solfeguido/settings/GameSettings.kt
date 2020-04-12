package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import ktx.json.readArrayValue
import ktx.json.readValue

class GameSettings : Json.Serializable {

    var keySignature: KeySignatureEnum = KeySignatureEnum.CMajor
    var measures: List<MeasureSettings> = emptyList()
    var generator: GeneratorSettings = GeneratorSettings()
    var time = TimeSettings()


    override fun write(json: Json) {
        json.writeValue("keySignature", keySignature)
        json.writeValue("measures", measures)
        json.writeValue("generator" ,generator)
        json.writeValue("time", time)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        keySignature = json.readValue(jsonData, "keySignature")
        measures = json.readArrayValue(jsonData, "measures")
        generator = json.readValue(jsonData, "generator")
        time = json.readValue(jsonData, "time")
    }

}
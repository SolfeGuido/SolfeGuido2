package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.settings.GeneratorSettings
import io.github.solfeguido.settings.MeasureSettings
import ktx.json.*

class EarTrainingOptions : IGameModeOptions {

    var keySignature = KeySignatureEnum.CMajor
    var measure: MeasureSettings = MeasureSettings()
    var generator: GeneratorSettings = GeneratorSettings()

    override fun read(json: Json, jsonData: JsonValue) {
        keySignature = json.readValue(jsonData, "keySignature")
        measure = json.readValue(jsonData, "measure")
        generator = json.readValue(jsonData, "generator")
    }

    override fun write(json: Json) {
        json.writeValue("generator", generator)
        json.writeValue("measure", measure)
        json.writeValue("keySignature", keySignature)
    }
}
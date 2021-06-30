package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.settings.generator.IGeneratorOptions
import io.github.solfeguido.settings.generator.RandomGenerator
import ktx.json.readValue

class MeasureSettings(
    var clef: ClefEnum = ClefEnum.GClef,
    var signature: KeySignatureEnum = KeySignatureEnum.CMajor,
    var generator: IGeneratorOptions = RandomGenerator(50, 70)
) : Json.Serializable {

    override fun write(json: Json) {
        json.writeValue("clef", clef)
        json.writeValue("signature", signature)
        generator.write(json)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        clef = json.readValue(jsonData, "clef")
        signature = json.readValue(jsonData, "signature")
        generator = IGeneratorOptions.toInstance(json, jsonData)
    }

}

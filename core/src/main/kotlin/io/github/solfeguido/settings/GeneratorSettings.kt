package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.GeneratorTypeEnum
import io.github.solfeguido.settings.generator.*
import ktx.json.*

class GeneratorSettings : Json.Serializable {

    var type: GeneratorTypeEnum = GeneratorTypeEnum.Constant
    var options : IGeneratorOptions = ConstantOptions()


    override fun write(json: Json) {
        json.writeValue("type", type)
        json.writeValue("options", options)
    }

    override fun read(json: Json, jsonData: JsonValue) {
        type = json.readValue(jsonData, "type")
        options = when(type) {
            GeneratorTypeEnum.Constant -> json.readValue<ConstantOptions>(jsonData, "options")
            GeneratorTypeEnum.CustomMidi -> json.readValue<CustomMidiOptions>(jsonData, "options")
            GeneratorTypeEnum.MidiFile -> json.readValue<MidiFileOptions>(jsonData, "options")
            GeneratorTypeEnum.RangedRandom -> json.readValue<RangedRandomOptions>(jsonData, "options")
            GeneratorTypeEnum.Random -> json.readValue<RandomOptions>(jsonData, "options")
        }
    }

}
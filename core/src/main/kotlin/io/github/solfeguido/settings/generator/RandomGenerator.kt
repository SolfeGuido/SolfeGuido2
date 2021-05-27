package io.github.solfeguido.settings.generator

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.readValue
import kotlin.random.Random

class RandomGenerator(
    var minNote: Int = 0,
    var maxNote: Int = 1
) : IGeneratorOptions {

    override fun read(json: Json, jsonData: JsonValue) {
        minNote = json.readValue(jsonData, "minNote")
        maxNote = json.readValue(jsonData, "maxNote")
    }

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("minNote", minNote)
        json.writeValue("maxNote", maxNote)
    }

    override fun next() = Random.nextInt(minNote, maxNote)

}

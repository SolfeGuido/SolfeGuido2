package io.github.solfeguido.settings.generator

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.readValue
import kotlin.random.Random

class MidiGenerator(var filename: String = "") : IGeneratorOptions {

    override fun read(json: Json, jsonData: JsonValue) {
        filename = json.readValue(jsonData, "filename")
    }

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("filename", filename)
    }

    override fun next(): Int {
        //TODO
        return Random.nextInt()
    }

    override fun hasAccidentals() = true

}
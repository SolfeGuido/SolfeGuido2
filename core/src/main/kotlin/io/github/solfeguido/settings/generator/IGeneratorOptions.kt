package io.github.solfeguido.settings.generator

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.*

interface IGeneratorOptions : Json.Serializable {

    companion object {
        fun toInstance(json: Json, jsonData: JsonValue) : IGeneratorOptions {
            val type: String = json.readValue(jsonData,"className")
            return when(type) {
                RandomGenerator::class.java.simpleName -> RandomGenerator()
                MidiGenerator::class.java.simpleName -> MidiGenerator()
                else -> throw Exception("Invalid type '$type' of generator")
            }.also { it.read(json, jsonData) }
        }
    }

    override fun read(json: Json, jsonData: JsonValue) {}

    override fun write(json: Json) {
        json.writeValue("className", this.javaClass.simpleName)
    }

    fun next(): Int
}
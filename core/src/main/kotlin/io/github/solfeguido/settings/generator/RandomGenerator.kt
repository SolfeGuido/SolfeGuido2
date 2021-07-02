package io.github.solfeguido.settings.generator

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.readValue

class RandomGenerator(
    private var minNote: Int = 0,
    private var maxNote: Int = 1,
    private var generateAccidentals: Boolean = false
) : IGeneratorOptions {

    companion object {
        val ACCIDENTALS_NOTES = setOf(1, 3, 6, 8, 10)
    }

    private var noteRange: List<Int>

    init {
        noteRange = genNoteRange()
    }

    private fun genNoteRange() = if (generateAccidentals) {
        (minNote..maxNote).toList()
    } else {
        (minNote..maxNote).filterNot { ACCIDENTALS_NOTES.contains(it % 12) }
    }

    override fun read(json: Json, jsonData: JsonValue) {
        minNote = json.readValue(jsonData, "minNote")
        maxNote = json.readValue(jsonData, "maxNote")
        generateAccidentals = json.readValue(jsonData, "generateAccidentals")
        noteRange = genNoteRange()
    }

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("minNote", minNote)
        json.writeValue("maxNote", maxNote)
        json.writeValue("generateAccidentals", generateAccidentals)
    }

    override fun hasAccidentals() = generateAccidentals

    override fun next() = noteRange.random()

}

package io.github.solfeguido2.settings.generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
@SerialName("randomGenerator")
class RandomGenerator(
    private var minNote: Int = 0,
    private var maxNote: Int = 1,
    private var generateAccidentals: Boolean = false
) : IGeneratorOptions {

    companion object {
        val ACCIDENTALS_NOTES = setOf(1, 3, 6, 8, 10)
    }

    @Transient
    private var noteRange: List<Int> = genNoteRange()


    private fun genNoteRange() = if (generateAccidentals) {
        (minNote..maxNote).toList()
    } else {
        (minNote..maxNote).filterNot { ACCIDENTALS_NOTES.contains(it % 12) }
    }


    override fun hasAccidentals() = generateAccidentals

    override fun next() = noteRange.random()

}

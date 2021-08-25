package io.github.solfeguido2.settings.gamemode

import io.github.solfeguido2.actors.MeasureActor
import io.github.solfeguido2.enums.NoteAccidentalEnum
import io.github.solfeguido2.enums.NoteStyle
import io.github.solfeguido2.settings.MeasureSettings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ktx.collections.toGdxArray

@Serializable
@SerialName("noteGuess")
class NoteGuessOptions(
    var measures: List<MeasureSettings> = listOf(),
    var isCustom: Boolean = true
) : IGameModeOptions {


    override fun generateMeasures(noteStyle: NoteStyle) = measures.map {
        MeasureActor(it, noteStyle)
    }.toGdxArray()

    override fun hasAccidentals(): Boolean {
        return measures.any { it.signature.symbol != NoteAccidentalEnum.Natural || it.generator.hasAccidentals() }
    }

}
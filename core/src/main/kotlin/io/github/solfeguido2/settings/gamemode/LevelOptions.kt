package io.github.solfeguido2.settings.gamemode

import io.github.solfeguido2.actors.MeasureActor
import io.github.solfeguido2.structures.progression.Level
import io.github.solfeguido2.enums.NoteAccidentalEnum
import io.github.solfeguido2.enums.NoteStyle
import io.github.solfeguido2.settings.MeasureSettings
import io.github.solfeguido2.settings.generator.RandomGenerator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ktx.collections.toGdxArray

@Serializable
@SerialName("level")
class LevelOptions(
    val level: Level,
    var isCustom: Boolean = false
) : IGameModeOptions {

    val measures = listOf(
        MeasureSettings(
            level.clef,
            generator = RandomGenerator(
                level.requirements.lowerNote,
                level.requirements.higherNote,
                level.requirements.hasAccidentals
            )
        )
    )

    override fun generateMeasures(noteStyle: NoteStyle) = measures.map {
        MeasureActor(it, noteStyle)
    }.toGdxArray()

    override fun hasAccidentals(): Boolean {
        return measures.any { it.signature.symbol != NoteAccidentalEnum.Natural || it.generator.hasAccidentals() }
    }
}
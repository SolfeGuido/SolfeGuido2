package io.github.solfeguido2.settings.gamemode

import io.github.solfeguido2.actors.MeasureActor
import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.enums.KeySignatureEnum
import io.github.solfeguido2.enums.NoteStyle
import io.github.solfeguido2.settings.MeasureSettings
import io.github.solfeguido2.settings.generator.RandomGenerator
import ktx.collections.GdxArray

sealed interface IGameModeOptions {

    companion object {

        fun classicGame(clef: ClefEnum) = NoteGuessOptions(
            measures = listOf(
                MeasureSettings(
                    signature = KeySignatureEnum.CMajor,
                    clef = clef,
                    generator = RandomGenerator(60, 90)
                )
            ),
            isCustom = false
        )


    }

    fun generateMeasures(noteStyle: NoteStyle): GdxArray<MeasureActor>

    fun hasAccidentals(): Boolean

}
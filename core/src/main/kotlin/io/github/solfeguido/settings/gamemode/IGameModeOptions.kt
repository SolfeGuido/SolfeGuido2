package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.scenes.scene2d.Actor
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.enums.NoteStyle
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.RandomGenerator
import io.github.solfeguido.events.ResultEvent
import ktx.collections.GdxArray
import ktx.inject.Context
import ktx.scene2d.KStack

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
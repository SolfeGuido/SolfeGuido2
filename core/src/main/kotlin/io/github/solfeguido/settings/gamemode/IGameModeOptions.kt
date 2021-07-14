package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.RandomGenerator
import io.github.solfeguido.ui.events.ResultEvent
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

    fun populateScene(context: Context, parent: KStack, resultCallback: (ResultEvent) -> Unit)

    fun validateNote(note: NoteOrderEnum): Boolean

    fun hasAccidentals(): Boolean

    fun endGame(context: Context, score: Int)

}
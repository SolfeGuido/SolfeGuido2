package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.IGeneratorOptions
import io.github.solfeguido.settings.generator.RandomGenerator
import io.github.solfeguido.ui.events.ResultEvent
import kotlinx.serialization.Serializable
import ktx.inject.Context
import ktx.scene2d.KStack

@Serializable
class EarTrainingOptions(
    var keySignature : KeySignatureEnum = KeySignatureEnum.CMajor,
    var measure: MeasureSettings = MeasureSettings(),
    var generator: IGeneratorOptions = RandomGenerator()
) : IGameModeOptions {

    override fun populateScene(context: Context, parent: KStack, resultCallback: (ResultEvent) -> Unit) {
        // TODO
    }

    override fun endGame(context: Context, score: Int) {
        // TODO
    }

    override fun hasAccidentals(): Boolean {
        TODO("Not yet implemented")
    }

    override fun validateNote(note: NoteOrderEnum) = false
}
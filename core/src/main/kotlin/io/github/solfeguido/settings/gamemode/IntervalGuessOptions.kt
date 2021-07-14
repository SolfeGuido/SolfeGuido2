package io.github.solfeguido.settings.gamemode


import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.ui.events.ResultEvent
import ktx.inject.Context
import ktx.scene2d.KStack

class IntervalGuessOptions(
    var minInterval: Int = 1,
    var maxInterval: Int = 12,
    var keySignature: KeySignatureEnum = KeySignatureEnum.CMajor
) : IGameModeOptions {

    override fun populateScene(context: Context, parent: KStack, resultCallback: (ResultEvent) -> Unit) {

    }

    override fun endGame(context: Context, score: Int) {
        // TODO
    }

    override fun hasAccidentals() = false

    override fun validateNote(note: NoteOrderEnum) = false
}
package io.github.solfeguido.settings.gamemode

import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.events.ResultEvent
import kotlinx.serialization.Serializable
import ktx.inject.Context
import ktx.scene2d.KStack

@Serializable
class KeySignatureGuessOptions(
    var signatures: Array<KeySignatureEnum> = arrayOf()
) : IGameModeOptions {

    override fun populateScene(context: Context, parent: KStack, resultCallback: (ResultEvent) -> Unit) {

    }

    override fun endGame(context: Context, score: Int) {
        // TODO
    }

    override fun hasAccidentals() = false

    override fun validateNote(note: NoteOrderEnum) = false

}
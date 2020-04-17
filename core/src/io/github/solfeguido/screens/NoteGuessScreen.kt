package io.github.solfeguido.screens

import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.settings.gamemode.NoteGuessOptions
import ktx.inject.Context

class NoteGuessScreen(context: Context) : UIScreen(context) {

    override fun create(settings: StateParameter) {
        val settings: NoteGuessOptions = settings.getValue()

    }
}
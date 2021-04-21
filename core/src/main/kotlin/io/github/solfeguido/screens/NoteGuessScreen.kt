package io.github.solfeguido.screens

import io.github.solfeguido.core.StateParameter
import io.github.solfeguido.settings.gamemode.NoteGuessOptions
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.scene2d.*

class NoteGuessScreen(context: Context) : UIScreen(context) {

    override fun create(settings: StateParameter) {
        val options: NoteGuessOptions = settings.getValue()
        stage += scene2d.table {

        }
    }
}
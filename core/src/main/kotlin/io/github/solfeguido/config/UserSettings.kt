package io.github.solfeguido.config

import io.github.solfeguido.enums.ButtonStyle
import io.github.solfeguido.enums.NoteStyle
import io.github.solfeguido.enums.Theme
import io.github.solfeguido.enums.Vibrations
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    var theme: Theme = Theme.Light,
    var vibrations: Vibrations = Vibrations.Enabled,
    var noteStyle: NoteStyle = NoteStyle.EnglishNotes,
    var buttonStyle: ButtonStyle = ButtonStyle.NotesButton,
    // Sound enabled or level
)
package io.github.solfeguido.structures

import io.github.solfeguido.enums.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserSettings(
    var theme: Theme = Theme.Light,
    var vibrations: Vibrations = Vibrations.Enabled,
    var noteStyle: NoteStyle = NoteStyle.EnglishNotes,
    var buttonStyle: ButtonStyle = ButtonStyle.NotesButton,
    var language: Language = Language.fromLocale(Locale.getDefault(Locale.Category.DISPLAY))
    // Sound enabled or level
)
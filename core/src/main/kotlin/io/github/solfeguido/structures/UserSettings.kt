package io.github.solfeguido.structures

import io.github.solfeguido.enums.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    var theme: Theme = Theme.Light,
    var vibrations: Vibrations = Vibrations.Enabled,
    var noteStyle: NoteStyle = NoteStyle.EnglishNotes,
    var buttonStyle: ButtonStyle = ButtonStyle.NotesButton,
    var language: Language = Language.English
    // Sound enabled or level
)
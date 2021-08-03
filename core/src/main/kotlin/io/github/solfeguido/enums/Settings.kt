package io.github.solfeguido.enums

sealed interface SettingsEnum


enum class Theme : SettingsEnum {
    Light,
    Dark;
}

enum class Vibrations(val value: Int) : SettingsEnum {
    Disabled(0),
    Enabled(1);
}

enum class NoteStyle : SettingsEnum {
    RomanNotes,
    LatinNotes,
    EnglishNotes;
}

enum class ButtonStyle : SettingsEnum {
    NotesButton,
    PianoKeys,
    PianoWithNotes;
}

enum class Language(val code: String) : SettingsEnum {
    English("en"),
    French("fr"),
    Spanish("es"),
    Italian("it"),
    Swedish("sv")
}
package io.github.solfeguido.enums

import java.util.*

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
    Swedish("sv");

    companion object {
        fun fromLocale(locale: Locale) : Language {
            val code = locale.language
            for(lang in values()) {
                if(lang.code == code) return lang
            }
            return English
        }
    }
}

enum class SoundEnabled(val volume: Float): SettingsEnum {
    Enabled(1f),
    Disabled(0f)
}
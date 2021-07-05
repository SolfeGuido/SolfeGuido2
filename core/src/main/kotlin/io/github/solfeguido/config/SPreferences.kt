package io.github.solfeguido.config

import com.badlogic.gdx.Preferences
import io.github.solfeguido.factories.enum
import ktx.preferences.set


class SPreferences(private val preferences: Preferences) {

    var theme: Theme = preferences.enum()
        private set(value) {
            this.preferences[Theme::class.java.simpleName] = value
            field = value
        }
    var vibrations: Vibrations = preferences.enum()
        private set(value) {
            this.preferences[Vibrations::class.java.simpleName] = value
            field = value
        }
    var noteStyle: NoteStyle = preferences.enum()
        private set(value) {
            this.preferences[NoteStyle::class.java.simpleName] = value
            field = value
        }
    var buttonStyle: ButtonStyle = preferences.enum()
        private set(value) {
            this.preferences[ButtonStyle::class.java.simpleName] = value
            field = value
        }

    fun save() {
        preferences.flush()
    }

    internal inline fun <reified T : Enum<T>> get() = when (T::class.java.simpleName) {
        Theme::class.java.simpleName -> this.theme
        Vibrations::class.java.simpleName -> this.vibrations
        NoteStyle::class.java.simpleName -> this.noteStyle
        ButtonStyle::class.java.simpleName -> this.buttonStyle
        else -> throw Error("Unknown preference ${T::class.java.simpleName}")
    }

    internal inline fun <T : Enum<T>> set(value: T) = when (value) {
        is Theme -> this.theme = value
        is Vibrations -> this.vibrations = value
        is NoteStyle -> this.noteStyle = value
        is ButtonStyle -> this.buttonStyle = value
        else -> throw Error("Cannot set preference ${value::class.java.name} with value $value")
    }

    enum class Theme {
        Light,
        Dark
    }

    enum class Vibrations(val value: Int) {
        Disabled(0),
        Enabled(1)
    }

    enum class NoteStyle {
        RomanNotes,
        LatinNotes,
        EnglishNotes
    }

    enum class ButtonStyle {
        NotesButton,
        PianoKeys,
        PianoWithNotes
    }

}
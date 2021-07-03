package io.github.solfeguido.config

import com.badlogic.gdx.Preferences
import io.github.solfeguido.factories.enum


class SPreferences(private val preferences: Preferences) {

    var theme: Theme private set
    var vibrations: Vibrations private set
    var noteStyle: NoteStyle private set
    var buttonStyle: ButtonStyle private set

    init {
        theme = preferences.enum()
        vibrations = preferences.enum()
        noteStyle = preferences.enum()
        buttonStyle = preferences.enum()
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
        is Vibrations -> this.vibrations
        is NoteStyle -> this.noteStyle
        is ButtonStyle -> this.buttonStyle
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
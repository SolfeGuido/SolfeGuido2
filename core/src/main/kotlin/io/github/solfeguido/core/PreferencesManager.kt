package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import io.github.solfeguido.config.Constants
import io.github.solfeguido.config.UserSettings
import io.github.solfeguido.enums.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ktx.preferences.get
import ktx.preferences.set


class PreferencesManager(private val preferences: Preferences) {

    private val settings: UserSettings

    var theme: Theme
        get() = settings.theme
        set(value) {
            settings.theme = value
        }
    var vibrations: Vibrations
        get() = settings.vibrations
        set(value) {
            settings.vibrations = value
        }
    var noteStyle: NoteStyle
        get() = settings.noteStyle
        private set(value) {
            settings.noteStyle = value
        }
    var buttonStyle: ButtonStyle
        get() = settings.buttonStyle
        private set(value) {
            settings.buttonStyle = value
        }

    fun get(default: SettingsEnum) = when (default) {
        is Theme -> theme
        is Vibrations -> vibrations
        is NoteStyle -> noteStyle
        is ButtonStyle -> buttonStyle
    }

    fun set(value: SettingsEnum) {
        when (value) {
            is Theme -> theme = value
            is Vibrations -> vibrations = value
            is NoteStyle -> noteStyle = value
            is ButtonStyle -> buttonStyle = value
        }
    }

    fun save() {
        preferences[Constants.Preferences.SETTINGS] = Json.encodeToString( settings)
        preferences.flush()
    }

    init {
        val prefValue: String? = preferences[Constants.Preferences.SETTINGS]
        settings = if (prefValue.isNullOrBlank()) {
            UserSettings()
        } else {
            Json.decodeFromString(prefValue)
        }

    }
}
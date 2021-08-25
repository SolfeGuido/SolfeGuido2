package io.github.solfeguido2.core

import com.badlogic.gdx.Preferences
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.structures.UserSettings
import io.github.solfeguido2.enums.*
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

    var language: Language
        get() = settings.language
        private set(value) {
            settings.language = value
        }

    var soundEnabled: SoundEnabled
        get() = settings.soundEnabled
        private set(value) {
            settings.soundEnabled = value
        }

    fun get(default: SettingsEnum) = when (default) {
        is Theme -> theme
        is Vibrations -> vibrations
        is NoteStyle -> noteStyle
        is ButtonStyle -> buttonStyle
        is Language -> language
        is SoundEnabled -> soundEnabled
    }

    fun set(value: SettingsEnum) {
        when (value) {
            is Theme -> theme = value
            is Vibrations -> vibrations = value
            is NoteStyle -> noteStyle = value
            is ButtonStyle -> buttonStyle = value
            is Language -> language = value
            is SoundEnabled -> soundEnabled = value
        }
    }

    fun save() {
        preferences[Constants.Preferences.SETTINGS] = Json.encodeToString(settings)
        preferences.flush()
    }

    init {
        val prefValue: String? = preferences[Constants.Preferences.SETTINGS]
        settings = if (prefValue.isNullOrBlank()) {
            UserSettings()
        } else {
            try {
                Json.decodeFromString(prefValue)
            } catch (ex: Exception) {
                error(ex)
                UserSettings()
            }
        }

    }
}
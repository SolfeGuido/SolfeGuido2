package io.github.solfeguido.enums

enum class PreferenceEnum(val value: Int) {
    // Theme
    Light(0),
    Dark(1),

    // Vibrations
    Disabled(0),
    Enabled(1),

    // Note style
    RomanNotes(0),
    LatinNotes(1),
    EnglishNotes(2),

    // Button style
    NotesButton(0),
    PianoKeys(1),
    PianoWithNotes(2);


    fun equals(other: Int?): Boolean {
        if (other is Int) return this.value == other
        return false
    }


}
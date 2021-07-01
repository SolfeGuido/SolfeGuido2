package io.github.solfeguido.enums


object SolfeGuidoPreferences {

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
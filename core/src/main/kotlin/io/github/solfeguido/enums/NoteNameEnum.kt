package io.github.solfeguido.enums

import ktx.collections.gdxMapOf

enum class NoteNameEnum(val value: String, val orderEnum: NoteOrderEnum) {

    C("C", NoteOrderEnum.C),
    D("D", NoteOrderEnum.D),
    E("E", NoteOrderEnum.E),
    F("F", NoteOrderEnum.F),
    G("G", NoteOrderEnum.G),
    A("A", NoteOrderEnum.A),
    B("B", NoteOrderEnum.B),
    None("NONE", NoteOrderEnum.C);

    companion object {
        private val NOTE_NAMES = gdxMapOf(
            SolfeGuidoPreferences.NoteStyle.LatinNotes to gdxMapOf(
                "C" to "Do",
                "D" to "Re",
                "E" to "Mi",
                "F" to "Fa",
                "G" to "Sol",
                "A" to "La",
                "B" to "Si"
            ),
            SolfeGuidoPreferences.NoteStyle.RomanNotes to gdxMapOf(
                "C" to "Do",
                "D" to "Ré",
                "E" to "Mi",
                "F" to "Fa",
                "G" to "Sol",
                "A" to "La",
                "B" to "Si"
            ),
        )

        operator fun get(name: String, noteStyle: SolfeGuidoPreferences.NoteStyle): String {
            return NOTE_NAMES[noteStyle]?.get(name) ?: name
        }
    }

    fun isNone() = this == None

}
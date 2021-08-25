package io.github.solfeguido2.enums

enum class NoteAccidentalEnum(val value: String, val toneEffect: Int) {
    Natural("", 0),
    ForceNatural("%", 0),
    Flat("b", -1),
    Sharp("#", 1)
}
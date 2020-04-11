package io.github.solfeguido.enums

enum class NoteAccidentalEnum(val value: String, val toneEffect: Int) {
    Natural("", 0),
    ForceNatural("%", 0),
    Flat("b", -1),
    Sharp("#", 1)
}
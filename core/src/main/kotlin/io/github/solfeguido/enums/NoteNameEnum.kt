package io.github.solfeguido.enums

enum class NoteNameEnum(val value: String, val orderEnum: NoteOrderEnum) {
    C("C", NoteOrderEnum.C),
    D("D", NoteOrderEnum.D),
    E("E", NoteOrderEnum.E),
    F("F", NoteOrderEnum.F),
    G("G", NoteOrderEnum.G),
    A("A", NoteOrderEnum.A),
    B("B", NoteOrderEnum.B),
    None("NONE", NoteOrderEnum.C);

    fun isNone() = this == None
}
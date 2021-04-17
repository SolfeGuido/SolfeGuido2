package io.github.solfeguido.enums

import java.lang.Error

enum class NoteOrderEnum(val index: Int) {
    C(0),
    CSharp(1),
    D(2),
    DSharp(3),
    E(4),
    F(5),
    FSharp(6),
    G(7),
    GSharp(8),
    A(9),
    ASharp(10),
    B(11);

    companion object {

        fun fromIndex(idx: Int): NoteOrderEnum = when (idx) {
            0 -> C
            1 -> CSharp
            2 -> D
            3 -> DSharp
            4 -> E
            5 -> F
            6 -> FSharp
            7 -> G
            8 -> GSharp
            9 -> A
            10 -> ASharp
            11 -> B
            else -> throw Error("No note equivalences for index $idx")
        }

    }
}
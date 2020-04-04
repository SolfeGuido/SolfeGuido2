package io.github.solfeguido.enums

import io.github.solfeguido.core.music.NoteAccidentalEnum

enum class KeySignatureEnum(val symbol: NoteAccidentalEnum, val numberOf: Int, val scale: ScaleEnum) {
    //Major scale
    CMajor(NoteAccidentalEnum.Sharp, 0, ScaleEnum.Major),
    GMajor(NoteAccidentalEnum.Sharp, 1, ScaleEnum.Major),
    DMajor(NoteAccidentalEnum.Sharp, 2, ScaleEnum.Major),
    AMajor(NoteAccidentalEnum.Sharp, 3, ScaleEnum.Major),
    EAMajor(NoteAccidentalEnum.Sharp, 4, ScaleEnum.Major),
    BAMajor(NoteAccidentalEnum.Sharp, 5, ScaleEnum.Major),
    FSharpMajor(NoteAccidentalEnum.Sharp, 6, ScaleEnum.Major),
    CSharpMajor(NoteAccidentalEnum.Sharp, 7, ScaleEnum.Major),

    CFlatMajor(NoteAccidentalEnum.Flat, 7, ScaleEnum.Major),
    GFlatMajor(NoteAccidentalEnum.Flat, 6, ScaleEnum.Major),
    DFlatMajor(NoteAccidentalEnum.Flat, 5, ScaleEnum.Major),
    AFlatMajor(NoteAccidentalEnum.Flat, 4, ScaleEnum.Major),
    EFlatMajor(NoteAccidentalEnum.Flat, 3, ScaleEnum.Major),
    BFlatMajor(NoteAccidentalEnum.Flat, 2, ScaleEnum.Major),
    FMajor(NoteAccidentalEnum.Flat, 1, ScaleEnum.Major),

    //Minor scale
    AMinor(NoteAccidentalEnum.Sharp, 0, ScaleEnum.Minor),
    EMinor(NoteAccidentalEnum.Sharp, 1, ScaleEnum.Minor),
    BMinor(NoteAccidentalEnum.Sharp, 2, ScaleEnum.Minor),
    FSharpMinor(NoteAccidentalEnum.Sharp, 3, ScaleEnum.Minor),
    CSharpMinor(NoteAccidentalEnum.Sharp, 4, ScaleEnum.Minor),
    GSharpMinor(NoteAccidentalEnum.Sharp, 5, ScaleEnum.Minor),
    DSharpMinor(NoteAccidentalEnum.Sharp, 6, ScaleEnum.Minor),
    ASharpMinor(NoteAccidentalEnum.Sharp, 7, ScaleEnum.Minor),

    AFlatMinor(NoteAccidentalEnum.Flat, 7, ScaleEnum.Minor),
    EFlatMinor(NoteAccidentalEnum.Flat, 6, ScaleEnum.Minor),
    BFlatMinor(NoteAccidentalEnum.Flat, 5, ScaleEnum.Minor),
    FMinor(NoteAccidentalEnum.Flat, 4, ScaleEnum.Minor),
    CMinor(NoteAccidentalEnum.Flat, 3, ScaleEnum.Minor),
    GMinor(NoteAccidentalEnum.Flat, 2, ScaleEnum.Minor),
    DMinor(NoteAccidentalEnum.Flat, 1, ScaleEnum.Minor),
}
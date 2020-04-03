package io.github.solfeguido.enums

import io.github.solfeguido.core.music.NoteAccidentalEnum

enum class KeySignatureEnum(symbol: NoteAccidentalEnum, numberOf: Int, scale: ScaleEnum) {
    //Major scale
    CMajor(NoteAccidentalEnum.Sharp, 0, ScaleEnum.Major),
    GMajor(NoteAccidentalEnum.Sharp, 1, ScaleEnum.Major),
    DMajor(NoteAccidentalEnum.Sharp, 2, ScaleEnum.Major),
    AMajor(NoteAccidentalEnum.Sharp, 3, ScaleEnum.Major),
    EAMajor(NoteAccidentalEnum.Sharp, 4, ScaleEnum.Major),
    BAMajor(NoteAccidentalEnum.Sharp, 5, ScaleEnum.Major),
    FSharpAMajor(NoteAccidentalEnum.Sharp, 6, ScaleEnum.Major),
    CSharpAMajor(NoteAccidentalEnum.Sharp, 7, ScaleEnum.Major),

    CFlatAMajor(NoteAccidentalEnum.Flat, 7, ScaleEnum.Major),
    GFlatAMajor(NoteAccidentalEnum.Flat, 6, ScaleEnum.Major),
    DFlatAMajor(NoteAccidentalEnum.Flat, 5, ScaleEnum.Major),
    AFlatAMajor(NoteAccidentalEnum.Flat, 4, ScaleEnum.Major),
    EFlatAMajor(NoteAccidentalEnum.Flat, 3, ScaleEnum.Major),
    BFlatAMajor(NoteAccidentalEnum.Flat, 2, ScaleEnum.Major),
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
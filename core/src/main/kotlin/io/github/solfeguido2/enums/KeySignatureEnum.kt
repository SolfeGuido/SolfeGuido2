package io.github.solfeguido2.enums

import io.github.solfeguido2.structures.KeySignatureConfig
import io.github.solfeguido2.structures.PossibleNote

enum class KeySignatureEnum(
    val symbol: NoteAccidentalEnum,
    val numberOf: Int,
    val scale: ScaleEnum,
    val translation: Nls
) {

    //Major scale
    CMajor(NoteAccidentalEnum.Natural, 0, ScaleEnum.Major, Nls.KeySignatureCMajor),
    GMajor(NoteAccidentalEnum.Sharp, 1, ScaleEnum.Major, Nls.KeySignatureGMajor),
    DMajor(NoteAccidentalEnum.Sharp, 2, ScaleEnum.Major, Nls.KeySignatureDMajor),
    AMajor(NoteAccidentalEnum.Sharp, 3, ScaleEnum.Major, Nls.KeySignatureAMajor),
    EMajor(NoteAccidentalEnum.Sharp, 4, ScaleEnum.Major, Nls.KeySignatureEMajor),
    BMajor(NoteAccidentalEnum.Sharp, 5, ScaleEnum.Major, Nls.KeySignatureBMajor),
    FSharpMajor(NoteAccidentalEnum.Sharp, 6, ScaleEnum.Major, Nls.KeySignatureFSharpMajor),
    CSharpMajor(NoteAccidentalEnum.Sharp, 7, ScaleEnum.Major, Nls.KeySignatureCSharpMajor),

    CFlatMajor(NoteAccidentalEnum.Flat, 7, ScaleEnum.Major, Nls.KeySignatureCFlatMajor),
    GFlatMajor(NoteAccidentalEnum.Flat, 6, ScaleEnum.Major, Nls.KeySignatureGFlatMajor),
    DFlatMajor(NoteAccidentalEnum.Flat, 5, ScaleEnum.Major, Nls.KeySignatureDFlatMajor),
    AFlatMajor(NoteAccidentalEnum.Flat, 4, ScaleEnum.Major, Nls.KeySignatureAFlatMajor),
    EFlatMajor(NoteAccidentalEnum.Flat, 3, ScaleEnum.Major, Nls.KeySignatureEFlatMajor),
    BFlatMajor(NoteAccidentalEnum.Flat, 2, ScaleEnum.Major, Nls.KeySignatureBFlatMajor),
    FMajor(NoteAccidentalEnum.Flat, 1, ScaleEnum.Major, Nls.KeySignatureFMajor),

    //Minor scale
    AMinor(NoteAccidentalEnum.Natural, 0, ScaleEnum.Minor, Nls.KeySignatureAMinor),
    EMinor(NoteAccidentalEnum.Sharp, 1, ScaleEnum.Minor, Nls.KeySignatureEMinor),
    BMinor(NoteAccidentalEnum.Sharp, 2, ScaleEnum.Minor, Nls.KeySignatureBMinor),
    FSharpMinor(NoteAccidentalEnum.Sharp, 3, ScaleEnum.Minor, Nls.KeySignatureFSharpMinor),
    CSharpMinor(NoteAccidentalEnum.Sharp, 4, ScaleEnum.Minor, Nls.KeySignatureCSharpMinor),
    GSharpMinor(NoteAccidentalEnum.Sharp, 5, ScaleEnum.Minor, Nls.KeySignatureGSharpMinor),
    DSharpMinor(NoteAccidentalEnum.Sharp, 6, ScaleEnum.Minor, Nls.KeySignatureDSharpMinor),
    ASharpMinor(NoteAccidentalEnum.Sharp, 7, ScaleEnum.Minor, Nls.KeySignatureASharpMinor),

    AFlatMinor(NoteAccidentalEnum.Flat, 7, ScaleEnum.Minor, Nls.KeySignatureAFlatMinor),
    EFlatMinor(NoteAccidentalEnum.Flat, 6, ScaleEnum.Minor, Nls.KeySignatureEFlatMinor),
    BFlatMinor(NoteAccidentalEnum.Flat, 5, ScaleEnum.Minor, Nls.KeySignatureBFlatMinor),
    FMinor(NoteAccidentalEnum.Flat, 4, ScaleEnum.Minor, Nls.KeySignatureFMinor),
    CMinor(NoteAccidentalEnum.Flat, 3, ScaleEnum.Minor, Nls.KeySignatureCMinor),
    GMinor(NoteAccidentalEnum.Flat, 2, ScaleEnum.Minor, Nls.KeySignatureGMinor),
    DMinor(NoteAccidentalEnum.Flat, 1, ScaleEnum.Minor, Nls.KeySignatureDMinor);

    val scaleNotes
        get() = when (scale) {
            ScaleEnum.Major -> KeySignatureConfig.MAJOR_SCALE_NOTES.take(numberOf)
            ScaleEnum.Minor -> KeySignatureConfig.MINOR_SCALE_NOTES.take(numberOf)
        }

    fun changesNote(note: NoteNameEnum) = scaleNotes.contains(note)

    fun extractNoteName(note: PossibleNote): NoteNameEnum {
        if (!note.hasNaturalNote()) return note.firstName(symbol)
        return note.naturalNote
    }

}
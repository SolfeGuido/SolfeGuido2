package io.github.solfeguido.config

import io.github.solfeguido.core.MidiNote
import io.github.solfeguido.enums.*
import io.github.solfeguido.factories.MidiNotePool
import ktx.collections.gdxArrayOf
import ktx.collections.gdxMapOf
import kotlin.math.sign

object KeySignatureConfig {

    /**
     * Position on the measure for the sharps,
     * when the measure's clef is the G Clef
     */
    val SHARP_ORDER = gdxArrayOf(8, 5, 9, 6, 3, 7, 4)

    /**
     * Position on the measure for the flat symbols
     * when the measure's clef is the G Clef
     */
    val FLAT_ORDER = gdxArrayOf(4, 7, 3, 6, 2, 5, 1)

    /**
     * Special case when using sharps and the CClef4
     */
    val CCLEF4_ORDER = gdxArrayOf(2, 6, 3, 7, 4, 8, 5)

    /**
     * How much to translate to have the correct position
     * of the symbols on the measure
     */
    val CLEF_TRANSLATE = gdxMapOf(
            ClefEnum.GClef to 0,
            ClefEnum.FClef to -2,
            ClefEnum.CClef3 to -1,
            ClefEnum.CClef4 to 1
    )

    val MAJOR_SCALE_NOTES = gdxArrayOf(
            NoteOrderEnum.F,
            NoteOrderEnum.C,
            NoteOrderEnum.G,
            NoteOrderEnum.D,
            NoteOrderEnum.B,
            NoteOrderEnum.E,
            NoteOrderEnum.A
    )

    val MINOR_SCALE_NOTES = gdxArrayOf(
        NoteOrderEnum.B,
        NoteOrderEnum.E,
        NoteOrderEnum.A,
        NoteOrderEnum.D,
        NoteOrderEnum.G,
        NoteOrderEnum.C,
        NoteOrderEnum.F
    )

    fun getIcon(symbol: NoteAccidentalEnum) = when(symbol) {
        NoteAccidentalEnum.Sharp -> IconName.SharpAccidental
        NoteAccidentalEnum.Flat -> IconName.FlatAccidental
        NoteAccidentalEnum.Natural -> IconName.Empty
    }


    fun getScaleNotes(scale: ScaleEnum) = when(scale) {
        ScaleEnum.Major -> MAJOR_SCALE_NOTES
        else -> MINOR_SCALE_NOTES
    }

    fun getNoteAccidental(note: MidiNote, signature: KeySignatureEnum): NoteAccidentalEnum {
        val orderIndex = note.midiIndex % 12
        val scales = getScaleNotes(signature.scale)
        val isNatural = scales.take(signature.numberOf).any { it.index == orderIndex }
        if(isNatural) return NoteAccidentalEnum.Natural
        return MidiNote.accidentalOf(orderIndex)
    }
}
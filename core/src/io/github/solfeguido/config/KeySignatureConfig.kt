package io.github.solfeguido.config

import io.github.solfeguido.core.MidiNote
import io.github.solfeguido.enums.*
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
            NoteNameEnum.F,
            NoteNameEnum.C,
            NoteNameEnum.G,
            NoteNameEnum.D,
            NoteNameEnum.B,
            NoteNameEnum.E,
            NoteNameEnum.A
    )

    val MINOR_SCALE_NOTES = gdxArrayOf(
            NoteNameEnum.B,
            NoteNameEnum.E,
            NoteNameEnum.A,
            NoteNameEnum.D,
            NoteNameEnum.G,
            NoteNameEnum.C,
            NoteNameEnum.F
    )

    fun getIcon(symbol: NoteAccidentalEnum) = when(symbol) {
        NoteAccidentalEnum.Sharp -> IconName.SharpAccidental
        NoteAccidentalEnum.Flat -> IconName.FlatAccidental
        NoteAccidentalEnum.Natural -> IconName.Empty
        NoteAccidentalEnum.ForceNatural -> IconName.Natural
    }


    fun getNoteAccidental(note: MidiNote, signature: KeySignatureEnum): NoteAccidentalEnum {
        val noteName = note.getName(signature)
        if(signature.changesNote(noteName)) {
            if(note.canBeNatural()) return NoteAccidentalEnum.ForceNatural
            return NoteAccidentalEnum.Natural
        }
        return note.getDefaultAccidental()
        /*
        val scales = getScaleNotes(signature.scale)
        val isNatural = scales.take(signature.numberOf).any { it.index == (orderIndex + signature.symbol.toneEffect) }
        if(isNatural) return NoteAccidentalEnum.Natural
        val accidental = MidiNote.accidentalOf(orderIndex, signature)
        if(accidental == NoteAccidentalEnum.Natural) {
            if(scales.take(signature.numberOf).any { it.index == orderIndex }) return NoteAccidentalEnum.ForceNatural
        }
        return accidental
        */
    }
}
package io.github.solfeguido2.structures

import com.badlogic.gdx.utils.Pool
import io.github.solfeguido2.enums.KeySignatureEnum
import io.github.solfeguido2.enums.NoteNameEnum
import io.github.solfeguido2.enums.NoteAccidentalEnum
import io.github.solfeguido2.enums.NoteOrderEnum
import ktx.collections.gdxArrayOf

data class MidiNote(
    var midiIndex: Int = 60
) : Pool.Poolable {

    // Idea: add 'preferFlat' boolean if use flat over sharp when possible
    private val noteName
        get() = NOTE_NAMES[midiIndex % MIDI_OCTAVE]

    val noteOrder get() = NoteOrderEnum.fromIndex(midiIndex % MIDI_OCTAVE)


    companion object {
        private val NOTE_NAMES = gdxArrayOf(
            NaturalOrSharpNote(NoteNameEnum.C, NoteNameEnum.B),
            FlatOrSharpNote(NoteNameEnum.D, NoteNameEnum.C),
            ConstantNote(NoteNameEnum.D),
            FlatOrSharpNote(NoteNameEnum.E, NoteNameEnum.D),
            NaturalOrFlatNote(NoteNameEnum.E, NoteNameEnum.F),
            NaturalOrSharpNote(NoteNameEnum.F, NoteNameEnum.E),
            FlatOrSharpNote(NoteNameEnum.G, NoteNameEnum.F),
            ConstantNote(NoteNameEnum.G),
            FlatOrSharpNote(NoteNameEnum.A, NoteNameEnum.G),
            ConstantNote(NoteNameEnum.A),
            FlatOrSharpNote(NoteNameEnum.B, NoteNameEnum.A),
            NaturalOrFlatNote(NoteNameEnum.B, NoteNameEnum.C)
        )

        val DEFAULT_NOTE = MidiNote()

        private val MIDI_OCTAVE = NOTE_NAMES.size

        fun naturalIndexOf(name: NoteNameEnum) =
            NOTE_NAMES.indexOfFirst { it.naturalNote == name }
    }

    fun getMeasurePosition(base: Int, signature: KeySignatureEnum): Int {
        var position = 0
        var firstNote = signature.extractNoteName(NOTE_NAMES[base % MIDI_OCTAVE])
        for (i in base..midiIndex) {
            val nwNote = signature.extractNoteName(NOTE_NAMES[i % MIDI_OCTAVE])
            if (firstNote != nwNote && nwNote != NoteNameEnum.None) {
                firstNote = nwNote
                position++
            }
        }
        return position
    }


    fun canBeNatural() = noteName.hasNaturalNote()

    fun getDefaultAccidental() = noteName.firstAccidental(
        NoteAccidentalEnum.Natural,
        NoteAccidentalEnum.Sharp,
        NoteAccidentalEnum.Flat
    )

    fun getName(keySignature: KeySignatureEnum): NoteNameEnum {
        return when (keySignature.symbol) {
            NoteAccidentalEnum.Flat -> noteName.firstName(
                NoteAccidentalEnum.Flat,
                NoteAccidentalEnum.Natural,
                NoteAccidentalEnum.Sharp
            )
            NoteAccidentalEnum.Sharp -> noteName.firstName(
                NoteAccidentalEnum.Sharp,
                NoteAccidentalEnum.Natural,
                NoteAccidentalEnum.Flat
            )
            else -> noteName.firstName(
                NoteAccidentalEnum.Natural,
                NoteAccidentalEnum.Sharp,
                NoteAccidentalEnum.Flat
            )
        }
    }

    operator fun plusAssign(other: MidiNote) {
        plusAssign(other.midiIndex)
    }

    operator fun minusAssign(other: MidiNote) {
        minusAssign(other.midiIndex)
    }

    operator fun plusAssign(other: Int) {
        this.midiIndex += other
    }

    operator fun minusAssign(other: Int) {
        this.midiIndex -= other
    }

    override fun equals(other: Any?) = (other is MidiNote) && other.midiIndex == midiIndex

    operator fun compareTo(other: MidiNote) = this.midiIndex - other.midiIndex

    override fun reset() {
        midiIndex = 60// Middle C
    }

    override fun toString(): String {
        return "$midiIndex"
    }

    fun fromData(
        name: NoteNameEnum,
        level: Int,
        accidental: NoteAccidentalEnum
    ): MidiNote {
        midiIndex = (level + 1) * MIDI_OCTAVE + naturalIndexOf(name) + accidental.toneEffect
        return this
    }


    fun fromString(str: String): MidiNote {
        if (str.length < 2) return this// Can't do much
        val noteName = NoteNameEnum.valueOf(str[0].toString())
        return when (val levelOrAcc = str[1]) {
            '#' -> fromData(noteName, str[2].toString().toInt(), NoteAccidentalEnum.Sharp)
            'b' -> fromData(noteName, str[2].toString().toInt(), NoteAccidentalEnum.Flat)
            else -> fromData(noteName, levelOrAcc.toString().toInt(), NoteAccidentalEnum.Natural)
        }
    }
}
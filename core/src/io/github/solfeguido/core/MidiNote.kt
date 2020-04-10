package io.github.solfeguido.core

import com.badlogic.gdx.utils.Pool
import io.github.solfeguido.config.ConstantNote
import io.github.solfeguido.config.FlatOrSharpNote
import io.github.solfeguido.config.NaturalOrFlatNote
import io.github.solfeguido.config.NaturalOrSharpNote
import io.github.solfeguido.config.PossibleNote
import io.github.solfeguido.enums.NoteNameEnum
import io.github.solfeguido.enums.NoteAccidentalEnum
import ktx.collections.gdxArrayOf
import ktx.log.info

data class MidiNote(
        var midiIndex: Int = 60
) : Pool.Poolable {

    // Idea: add 'preferFlat' boolean if use flat over sharp when possible

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
                FlatOrSharpNote(NoteNameEnum.F, NoteNameEnum.G),
                ConstantNote(NoteNameEnum.A),
                FlatOrSharpNote(NoteNameEnum.B, NoteNameEnum.A),
                NaturalOrFlatNote(NoteNameEnum.B, NoteNameEnum.C)
        )

        private val MIDI_OCTAVE: Int
                get() = NOTE_NAMES.size

        fun measurePosition(start: Int, target: Int) : Int {
            val relativeStart = start % MIDI_OCTAVE
            val diff = target - start
            var position = 0
            for(i in 0 until diff) {
                val note = NOTE_NAMES[(relativeStart + i) % MIDI_OCTAVE]
                if(note.hasNaturalNote()) position++
            }
            return position
        }

        fun naturalIndexOf(name: NoteNameEnum) = NOTE_NAMES.indexOfFirst {  it.getNaturalNote() == name  }

        fun accidentalOf(index: Int): NoteAccidentalEnum = NOTE_NAMES[index].let {
            if(it.getNaturalNote() != NoteNameEnum.None) return NoteAccidentalEnum.Natural
            return NoteAccidentalEnum.Sharp
        }
    }

    fun nextIndex() = this.also { midiIndex++ }

    fun prevIndex() = this.also { midiIndex-- }

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

    operator fun compareTo(other: MidiNote) =  this.midiIndex - other.midiIndex

    override fun reset() {
        midiIndex = 50
    }

    override fun toString(): String {
        return "$midiIndex"
    }

    fun fromData(
            name: NoteNameEnum,
            level: Int,
            accidental: NoteAccidentalEnum
        ) : MidiNote {
        midiIndex = (level + 1) * 12 + naturalIndexOf(name) + when(accidental) {
            NoteAccidentalEnum.Flat -> -1
            NoteAccidentalEnum.Natural  -> 0
            NoteAccidentalEnum.Sharp -> 1
        }
        return this
    }


    fun fromString(str: String) : MidiNote {
        if(str.length < 2) return this// Can't do much
        val noteName = NoteNameEnum.valueOf(str[0].toString())
        return when (val levelOrAcc = str[1]) {
            '#' -> fromData(noteName, str[2].toString().toInt(), NoteAccidentalEnum.Sharp )
            'b' -> fromData(noteName, str[2].toString().toInt(), NoteAccidentalEnum.Flat)
            else -> fromData(noteName, levelOrAcc.toString().toInt(), NoteAccidentalEnum.Natural)
        }
    }
}
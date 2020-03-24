package io.github.solfeguido.core

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.OrderedSet
import com.badlogic.gdx.utils.Pool
import io.github.solfeguido.core.music.*
import io.github.solfeguido.core.music.NoteNameEnum
import ktx.collections.gdxArrayOf
import ktx.collections.gdxSetOf
import ktx.log.info

data class MusicalNote(
        var midiIndex: Int
) : Pool.Poolable {

    companion object {
        private val NOTE_NAMES = gdxArrayOf<PossibleNote>(
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

        private fun naturalIndexOf(name: NoteNameEnum) = NOTE_NAMES.indexOfFirst {  it.getNaturalNote() == name  }
    }

    fun nextIndex() = this.also { midiIndex++ }

    fun prevIndex() = this.also { midiIndex-- }

    operator fun plusAssign(other: MusicalNote) {
        plusAssign(other.midiIndex)
    }

    operator fun minusAssign(other: MusicalNote) {
        minusAssign(other.midiIndex)
    }

    operator fun plusAssign(other: Int) {
        this.midiIndex += other
    }

    operator fun minusAssign(other: Int) {
        this.midiIndex -= other
    }

    override fun equals(other: Any?) = (other is MusicalNote) && other.midiIndex == midiIndex

    operator fun compareTo(other: MusicalNote) =  this.midiIndex - other.midiIndex

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
        ) : MusicalNote {
        midiIndex = (level + 1) * 12 + naturalIndexOf(name) + when(accidental) {
            NoteAccidentalEnum.Flat -> -1
            NoteAccidentalEnum.Natural  -> 0
            NoteAccidentalEnum.Sharp -> 1
        }
        return this
    }


    fun fromString(str: String) : MusicalNote {
        if(str.length < 2) return this// Can't do much
        val noteName = NoteNameEnum.valueOf(str[0].toString())
        return when (val levelOrAcc = str[1]) {
            '#' -> fromData(noteName, str[2].toString().toInt(), NoteAccidentalEnum.Sharp )
            'b' -> fromData(noteName, str[2].toString().toInt(), NoteAccidentalEnum.Flat)
            else -> fromData(noteName, levelOrAcc.toString().toInt(), NoteAccidentalEnum.Natural)
        }
    }
}
package io.github.solfeguido2.structures

import io.github.solfeguido2.enums.NoteAccidentalEnum
import io.github.solfeguido2.enums.NoteNameEnum

sealed class PossibleNote {

    override fun toString() = "(Natural:${naturalNote}, Sharp: ${sharpNote}, Flat: ${flatNote})";


    fun firstName(first: NoteAccidentalEnum, second: NoteAccidentalEnum = NoteAccidentalEnum.Natural, third: NoteAccidentalEnum = NoteAccidentalEnum.Natural) : NoteNameEnum {
        val firstNote = fromAccidental(first)
        if(!firstNote.isNone()) return firstNote
        val secondNote = fromAccidental(second)
        if(!secondNote.isNone()) return secondNote
        return fromAccidental(third)
    }

    fun firstAccidental(first: NoteAccidentalEnum, second: NoteAccidentalEnum = NoteAccidentalEnum.Natural, third: NoteAccidentalEnum = NoteAccidentalEnum.Natural): NoteAccidentalEnum {
        val firstNote = fromAccidental(first)
        if(!firstNote.isNone()) return first
        val secondNote = fromAccidental(second)
        if(!secondNote.isNone()) return second
        return third
    }

    private fun fromAccidental(accidentalEnum: NoteAccidentalEnum) = when(accidentalEnum) {
        NoteAccidentalEnum.Flat -> flatNote
        NoteAccidentalEnum.Sharp -> sharpNote
        else -> naturalNote
    }

    fun hasNaturalNote() = naturalNote != NoteNameEnum.None

    abstract val naturalNote : NoteNameEnum

    abstract val  flatNote : NoteNameEnum

    abstract val sharpNote: NoteNameEnum
}

class ConstantNote(override val naturalNote: NoteNameEnum) : PossibleNote() {
    override val flatNote = NoteNameEnum.None
    override val sharpNote = NoteNameEnum.None
}

class FlatOrSharpNote(override val flatNote: NoteNameEnum, override val sharpNote: NoteNameEnum) : PossibleNote() {
    override val naturalNote = NoteNameEnum.None
}

class NaturalOrSharpNote(override val naturalNote: NoteNameEnum, override val sharpNote: NoteNameEnum) : PossibleNote() {
    override val flatNote = NoteNameEnum.None
}

class NaturalOrFlatNote(override val naturalNote: NoteNameEnum, override val flatNote: NoteNameEnum): PossibleNote() {
    override val sharpNote = NoteNameEnum.None
}
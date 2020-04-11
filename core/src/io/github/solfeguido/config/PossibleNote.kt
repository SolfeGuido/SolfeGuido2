package io.github.solfeguido.config

import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.enums.NoteNameEnum

abstract class PossibleNote {

    override fun toString() = "(Natural:${getNaturalNote()}, Sharp: ${getSharpNote()}, Flat: ${getFlatNote()})";


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
        NoteAccidentalEnum.Flat -> getFlatNote()
        NoteAccidentalEnum.Sharp -> getSharpNote()
        else -> getNaturalNote()
    }

    fun hasNaturalNote() = getNaturalNote() != NoteNameEnum.None

    abstract fun getNaturalNote() : NoteNameEnum

    abstract fun  getFlatNote() : NoteNameEnum

    abstract fun  getSharpNote(): NoteNameEnum
}

class ConstantNote(private val constantName: NoteNameEnum) : PossibleNote() {
    override fun getNaturalNote() = constantName
    override fun getFlatNote() = NoteNameEnum.None
    override fun getSharpNote() = NoteNameEnum.None
}

class FlatOrSharpNote(private val flatNote: NoteNameEnum, private val sharpNote: NoteNameEnum) : PossibleNote() {
    override fun getNaturalNote() = NoteNameEnum.None
    override fun getFlatNote() = flatNote
    override fun getSharpNote() = sharpNote
}

class NaturalOrSharpNote(private val naturalNote: NoteNameEnum, private val sharpNote: NoteNameEnum) : PossibleNote() {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = NoteNameEnum.None
    override fun getSharpNote() = sharpNote
}

class NaturalOrFlatNote(private val naturalNote: NoteNameEnum, private val flatNote: NoteNameEnum): PossibleNote() {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = flatNote
    override fun getSharpNote() = NoteNameEnum.None
}
package io.github.solfeguido.core.music

import java.lang.Error

abstract class PossibleNote {

    abstract fun getNaturalNote() : NoteNameEnum

    abstract fun getFlatNote() : NoteNameEnum

    abstract fun getSharpNote(): NoteNameEnum
}

class ConstantNote(private val constantName: NoteNameEnum) : PossibleNote() {
    override fun getNaturalNote() = constantName
    override fun getFlatNote() = constantName
    override fun getSharpNote() = constantName
}

class FlatOrSharpNote(private val flatNote: NoteNameEnum, private val sharpNote: NoteNameEnum) : PossibleNote() {
    override fun getNaturalNote() = sharpNote//Sharp by default
    override fun getFlatNote() = flatNote
    override fun getSharpNote() = sharpNote
}

class NaturalOrFlatOrSharp(
        private val naturalNote: NoteNameEnum,
        private val flatNote: NoteNameEnum,
        private val sharpNote: NoteNameEnum
) : PossibleNote() {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = sharpNote
    override fun getSharpNote() = flatNote
}

class NaturalOrSharpNote(private val naturalNote: NoteNameEnum, private val sharpNote: NoteNameEnum) : PossibleNote() {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = naturalNote
    override fun getSharpNote() = sharpNote
}

class NaturalOrFlatNote(private val naturalNote: NoteNameEnum, private val flatNote: NoteNameEnum): PossibleNote() {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = flatNote
    override fun getSharpNote() = naturalNote

}
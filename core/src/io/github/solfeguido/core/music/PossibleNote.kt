package io.github.solfeguido.core.music

interface PossibleNote {

    fun getNaturalNote() : NoteNameEnum

    fun getFlatNote() : NoteNameEnum

    fun getSharpNote(): NoteNameEnum
}

class ConstantNote(private val constantName: NoteNameEnum) : PossibleNote {
    override fun getNaturalNote() = constantName
    override fun getFlatNote() = NoteNameEnum.None
    override fun getSharpNote() = NoteNameEnum.None
}

class FlatOrSharpNote(private val flatNote: NoteNameEnum, private val sharpNote: NoteNameEnum) : PossibleNote {
    override fun getNaturalNote() = NoteNameEnum.None
    override fun getFlatNote() = flatNote
    override fun getSharpNote() = sharpNote
}

class NaturalOrSharpNote(private val naturalNote: NoteNameEnum, private val sharpNote: NoteNameEnum) : PossibleNote {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = NoteNameEnum.None
    override fun getSharpNote() = sharpNote
}

class NaturalOrFlatNote(private val naturalNote: NoteNameEnum, private val flatNote: NoteNameEnum): PossibleNote {
    override fun getNaturalNote() = naturalNote
    override fun getFlatNote() = flatNote
    override fun getSharpNote() = NoteNameEnum.None

}
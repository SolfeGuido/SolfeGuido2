package io.github.solfeguido.utils

import com.badlogic.gdx.utils.Pool
import io.github.solfeguido.core.music.NoteNameEnum
import io.github.solfeguido.core.MusicalNote
import io.github.solfeguido.core.music.NoteAccidentalEnum
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.info

class NoteDataPool : Pool<MusicalNote>() {

    private val notesOrder: GdxArray<MusicalNote>

    init {
        notesOrder = gdxArrayOf(
                get(NoteNameEnum.C, 2, NoteAccidentalEnum.Natural),
                get(NoteNameEnum.C, 2, NoteAccidentalEnum.Sharp),
                get(NoteNameEnum.D, 2, NoteAccidentalEnum.Natural),
                get(NoteNameEnum.D, 2, NoteAccidentalEnum.Sharp),
                get(NoteNameEnum.E, 2, NoteAccidentalEnum.Natural),
                get(NoteNameEnum.F, 2, NoteAccidentalEnum.Natural),
                get(NoteNameEnum.F, 2, NoteAccidentalEnum.Sharp),
                get(NoteNameEnum.G, 2, NoteAccidentalEnum.Natural),
                get(NoteNameEnum.G, 2, NoteAccidentalEnum.Sharp),
                get(NoteNameEnum.A, 2, NoteAccidentalEnum.Natural),
                get(NoteNameEnum.A, 2, NoteAccidentalEnum.Sharp),
                get(NoteNameEnum.B, 2, NoteAccidentalEnum.Natural)
        )
    }

    override fun newObject(): MusicalNote {
        return MusicalNote(50)
    }

    fun get(name: NoteNameEnum, level: Int, accidentalEnum: NoteAccidentalEnum = NoteAccidentalEnum.Natural) : MusicalNote {
        return obtain().also { it.fromData(name, level, accidentalEnum) }
    }

    fun get(midiIndex : Int) = obtain().also { it.midiIndex = midiIndex }

    fun cloneNote(obj : MusicalNote) = obtain().also { it.midiIndex = obj.midiIndex }

    inline fun withString(str: String, action : (MusicalNote) -> Unit) {
        val note = fromString(str)
        action(note)
        free(note)
    }

    inline fun withIndex(idx : Int, action: (MusicalNote) -> Unit) {
        val note = get(idx)
        action(note)
        free(note)
    }

    fun fromString(str: String) = obtain().fromString(str)


}
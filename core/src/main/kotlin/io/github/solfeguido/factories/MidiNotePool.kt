package io.github.solfeguido.factories

import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.core.MidiNote
import io.github.solfeguido.enums.NoteAccidentalEnum
import io.github.solfeguido.enums.NoteNameEnum
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf

object MidiNotePool {

    val NOTES_ORDER: GdxArray<MidiNote>

    init {
        NOTES_ORDER = gdxArrayOf(
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

    fun obtain(): MidiNote = Pools.obtain(MidiNote::class.java)
    fun free(note: MidiNote) = Pools.free(note)

    fun get(name: NoteNameEnum, level: Int, accidentalEnum: NoteAccidentalEnum = NoteAccidentalEnum.Natural): MidiNote {
        return obtain().also { it.fromData(name, level, accidentalEnum) }
    }

    fun get(midiIndex: Int): MidiNote = obtain().also { it.midiIndex = midiIndex }

    fun cloneNote(obj: MidiNote): MidiNote = obtain().also { it.midiIndex = obj.midiIndex }

    inline fun withString(str: String, action: (MidiNote) -> Unit) {
        val note = fromString(str)
        action(note)
        free(note)
    }

    inline fun withIndex(idx: Int, action: (MidiNote) -> Unit) {
        val note = get(idx)
        action(note)
        free(note)
    }

    fun fromString(str: String) = obtain().fromString(str)

    fun fromIndex(midiIndex: Int): MidiNote = obtain().also { it.midiIndex = midiIndex }

}
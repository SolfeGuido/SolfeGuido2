package io.github.solfeguido2.factories

import com.badlogic.gdx.utils.Pools
import io.github.solfeguido2.actors.MeasureActor
import io.github.solfeguido2.actors.NoteActor
import io.github.solfeguido2.structures.MidiNote

object NoteActorPool {

    private fun obtain() = Pools.obtain(NoteActor::class.java)

    fun get() = obtain()

    fun generate(note: MidiNote, measure: MeasureActor): NoteActor = get().also {
        it.create(measure, note)
    }
}
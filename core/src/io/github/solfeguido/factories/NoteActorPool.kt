package io.github.solfeguido.factories

import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.actors.NoteActor
import io.github.solfeguido.core.MidiNote

object NoteActorPool {

    private fun obtain() = Pools.obtain(NoteActor::class.java)

    fun get() = obtain()

    fun generate(note: MidiNote, measure: MeasureActor) = get().also {
        it.create(measure, note)
    }
}
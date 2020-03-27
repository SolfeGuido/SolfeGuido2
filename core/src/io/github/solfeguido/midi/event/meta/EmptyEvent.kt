package io.github.solfeguido.midi.event.meta

import io.github.solfeguido.midi.event.MidiEvent
import io.github.solfeguido.midi.util.VariableLengthInt

class EmptyEvent(tick: Long, delta: Long) : MetaEvent(tick, delta, MetaEvent.EMPTY, VariableLengthInt(0)) {

    override val eventSize: Int
        get() = 0

    override fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return 1
        if(other.mTick != mTick) {
            return mTick.compareTo(other.mTick)
        }

        if(mDelta != other.mDelta) {
            return if (mDelta.varValue < other.delta) 1 else -1
        }

        return if(other !is EmptyEvent) 1 else 0
    }
}
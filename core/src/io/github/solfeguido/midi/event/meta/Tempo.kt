//////////////////////////////////////////////////////////////////////////////
//	Copyright 2011 Alex Leffelman
// Translated and adapted to kotlin by AzariasB 2020
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//	http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
//////////////////////////////////////////////////////////////////////////////
package io.github.solfeguido.midi.event.meta


import io.github.solfeguido.midi.event.MidiEvent
import io.github.solfeguido.midi.event.meta.GenericMetaEvent
import io.github.solfeguido.midi.event.meta.MetaEvent
import io.github.solfeguido.midi.util.VariableLengthInt
import io.github.solfeguido.midi.util.bytesToInt
import io.github.solfeguido.midi.util.intToBytes
import java.io.IOException
import java.io.OutputStream

class Tempo @JvmOverloads constructor(tick: Long = 0, delta: Long = 0, mpqn: Int = DEFAULT_MPQN) : MetaEvent(tick, delta, TEMPO, VariableLengthInt(3)) {
    private var mMPQN = 0
    private var mBPM = 0f
    var mpqn: Int
        get() = mMPQN
        set(m) {
            mMPQN = m
            mBPM = 60000000.0f / mMPQN
        }

    var bpm: Float
        get() = mBPM
        set(b) {
            mBPM = b
            mMPQN = (60000000 / mBPM).toInt()
        }

    override val eventSize: Int
        protected get() = 6

    public override fun writeToFile(out: OutputStream) {
        super.writeToFile(out)
        out.write(3)
        out.write(intToBytes(mMPQN, 3))
    }

    override fun compareTo(other: MidiEvent?): Int {
        if( other !is MidiEvent) return 1
        if (mTick != other.mTick) {
            return if (mTick < other.mTick) -1 else 1
        }
        if (mDelta.varValue.toLong() != other.delta) {
            return if (mDelta.varValue < other.delta) 1 else -1
        }
        if (other !is Tempo) {
            return 1
        }
        val o = other as Tempo
        return if (mMPQN != o.mMPQN) {
            if (mMPQN < o.mMPQN) -1 else 1
        } else 0
    }

    companion object {
        const val DEFAULT_BPM = 120.0f
        const val DEFAULT_MPQN = (60000000 / DEFAULT_BPM).toInt()
        fun parseTempo(tick: Long, delta: Long, info: MetaEventData): MetaEvent {
            if (info.length.varValue!= 3) {
                return GenericMetaEvent(tick, delta, info)
            }
            val mpqn: Int = bytesToInt(info.data, 0, 3)
            return Tempo(tick, delta, mpqn)
        }
    }

    init {
        this.mMPQN = mpqn
    }
}
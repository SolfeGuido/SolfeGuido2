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
import io.github.solfeguido.midi.util.VariableLengthInt
import java.io.IOException
import java.io.OutputStream


class SequenceNumber(tick: Long, delta: Long, val sequenceNumber: Int) : MetaEvent(tick, delta, SEQUENCE_NUMBER, VariableLengthInt(2)) {
    val mostSignificantBits: Int
        get() = sequenceNumber shr 8

    val leastSignificantBits: Int
        get() = sequenceNumber and 0xFF

    @Throws(IOException::class)
    public override fun writeToFile(out: OutputStream) {
        super.writeToFile(out)
        out.write(2)
        out.write(mostSignificantBits)
        out.write(leastSignificantBits)
    }

    override val eventSize: Int
        get() = 5

    override fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return 1
        if (mTick != other.mTick) {
            return if (mTick < other.mTick) -1 else 1
        }
        if (mDelta.varValue.toLong() != other.delta) {
            return if (mDelta.varValue < other.delta) 1 else -1
        }
        if (other !is SequenceNumber) {
            return 1
        }
        val o = other as SequenceNumber
        return if (sequenceNumber != o.sequenceNumber) {
            if (sequenceNumber < o.sequenceNumber) -1 else 1
        } else 0
    }

    companion object {
        fun parseSequenceNumber(tick: Long, delta: Long, info: MetaEventData): MetaEvent {
            if (info.length.varValue !== 2) {
                return GenericMetaEvent(tick, delta, info)
            }
            val msb = info.data[0].toInt()
            val lsb = info.data[1].toInt()
            val number = (msb shl 8) + lsb
            return SequenceNumber(tick, delta, number)
        }
    }

}
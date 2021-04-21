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
import java.io.OutputStream


class KeySignature(tick: Long, delta: Long, var key: Int, var scale: Int) : MetaEvent(tick, delta, KEY_SIGNATURE, VariableLengthInt(2)) {
    private var mKey = 0

    override val eventSize: Int
        protected get() = 5

    public override fun writeToFile(out: OutputStream) {
        super.writeToFile(out)
        out.write(2)
        out.write(mKey)
        out.write(scale)
    }

    override fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return 0;
        if (mTick != other.mTick) {
            return if (mTick < other.mTick) -1 else 1
        }
        if (mDelta.varValue.toLong() != other.delta) {
            return if (mDelta.varValue < other.delta) 1 else -1
        }
        if (other !is KeySignature) {
            return 1
        }
        val o = other as KeySignature
        if (mKey != o.mKey) {
            return if (mKey < o.mKey) -1 else 1
        }
        return if (scale != o.scale) {
            if (mKey < o.scale) -1 else 1
        } else 0
    }

    companion object {
        const val SCALE_MAJOR = 0
        const val SCALE_MINOR = 1
        fun parseKeySignature(tick: Long, delta: Long, info: MetaEventData): MetaEvent {
            if (info.length.varValue != 2) {
                return GenericMetaEvent(tick, delta, info)
            }
            val key = info.data[0].toInt()
            val scale = info.data[1].toInt()
            return KeySignature(tick, delta, key, scale)
        }
    }

}
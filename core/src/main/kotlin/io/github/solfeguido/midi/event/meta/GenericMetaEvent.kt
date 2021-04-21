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
import java.io.OutputStream

class GenericMetaEvent constructor(tick: Long, delta: Long, info: MetaEventData) : MetaEvent(tick, delta, info.type, info.length) {
    private val mData: ByteArray = info.data
    override val eventSize: Int
        get() = 1 + 1 + mLength.sizeInBytes + mLength.varValue

    override fun writeToFile(out: OutputStream) {
        super.writeToFile(out)
        out.write(mLength.bytes)
        out.write(mData)
    }

    override fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return -1;
        if (mTick != other.mTick) {
            return if (mTick < other.mTick) -1 else 1
        }
        return if (mDelta.varValue.toLong() != other.delta) {
            if (mDelta.varValue < other.delta) 1 else -1
        } else 1
    }

    init {
        println("""Warning: GenericMetaEvent used because type (${info.type}) wasn't recognized or unexpected data length (${info.length.varValue}) for type.""")
    }
}
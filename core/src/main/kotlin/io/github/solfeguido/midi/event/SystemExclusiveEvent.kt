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

package io.github.solfeguido.midi.event


import io.github.solfeguido.midi.util.VariableLengthInt
import java.io.IOException
import java.io.OutputStream


/**
 * Full Disclosure, SysEx events may not be properly handled by this library.
 *
 */
class SystemExclusiveEvent(type: Int, tick: Long, delta: Long, data: ByteArray) : MidiEvent(tick, delta) {
    private var mType: Int
    private val mLength: VariableLengthInt
    private var mData: ByteArray

    constructor(type: Int, tick: Long, data: ByteArray) : this(type, tick, 0, data) {}

    var data: ByteArray
        get() = mData
        set(data) {
            mLength.updateValue(data.size)
            mData = data
        }

    override fun requiresStatusByte(prevEvent: MidiEvent?): Boolean {
        return true
    }

    override fun writeToFile(out: OutputStream, writeType: Boolean) {
        super.writeToFile(out, writeType)
        out.write(mType)
        out.write(mLength.bytes)
        out.write(mData)
    }

    override operator fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return 1
        if (mTick < other.mTick) {
            return -1
        }
        if (mTick > other.mTick) {
            return 1
        }
        if (mDelta.varValue > other.mDelta.varValue) {
            return -1
        }
        if (mDelta.varValue < other.mDelta.varValue) {
            return 1
        }
        if (other is SystemExclusiveEvent) {
            val curr = String(mData)
            val comp = String(other.mData)
            return curr.compareTo(comp)
        }
        return 1
    }

    override val eventSize: Int
        get() = 1 + mLength.sizeInBytes + mData.size

    init {
        mType = type and 0xFF
        if (mType != 0xF0 && mType != 0xF7) {
            mType = 0xF0
        }
        mLength = VariableLengthInt(data.size)
        mData = data
    }
}
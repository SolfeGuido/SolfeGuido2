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


import io.github.solfeguido.midi.event.meta.MetaEvent
import io.github.solfeguido.midi.util.VariableLengthInt
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


abstract class MidiEvent(var tick: Long, delta: Long) : Comparable<MidiEvent?> {
    var mDelta: VariableLengthInt = VariableLengthInt(delta.toInt())
    var mTick: Int = 0
        private set

    var delta: Long
        get() = mDelta.varValue.toLong()
        set(d) {
            mDelta.updateValue(d.toInt())
        }

    protected abstract val eventSize: Int
    val size: Int
        get() = eventSize + mDelta.sizeInBytes

    open fun requiresStatusByte(prevEvent: MidiEvent?): Boolean {
        if (prevEvent == null) {
            return true
        }
        if (this is MetaEvent) {
            return true
        }
        return this.javaClass != prevEvent.javaClass
    }

    @Throws(IOException::class)
    open fun writeToFile(out: OutputStream, writeType: Boolean) {
        out.write(mDelta.bytes)
    }

    override fun toString(): String {
        return """$tick (${mDelta.varValue}): ${this.javaClass.simpleName}"""
    }

    companion object {
        private var sId = -1
        private var sType = -1
        private var sChannel = -1

        @Throws(IOException::class)
        fun parseEvent(tick: Long, delta: Long, input: InputStream): MidiEvent? {
            input.mark(1)
            var reset = false
            val id = input.read()
            if (!verifyIdentifier(id)) {
                input.reset()
                reset = true
            }
            if (sType in 0x8..0xE) {
                return ChannelEvent.parseChannelEvent(tick, delta, sType, sChannel, input)
            } else if (sId == 0xFF) {
                return MetaEvent.parseMetaEvent(tick, delta, input)
            } else if (sId == 0xF0 || sId == 0xF7) {
                val size = VariableLengthInt(input)
                val data = ByteArray(size.varValue)
                input.read(data)
                return SystemExclusiveEvent(sId, tick, delta, data)
            } else {
                println("Unable to handle status byte, skipping: $sId")
                if (reset) {
                    input.read()
                }
            }
            return null
        }

        private fun verifyIdentifier(id: Int): Boolean {
            sId = id
            val type = id shr 4
            val channel = id and 0x0F
            when {
                type in 0x8..0xE -> {
                    sId = id
                    sType = type
                    sChannel = channel
                }
                id == 0xFF -> {
                    sId = id
                    sType = -1
                    sChannel = -1
                }
                type == 0xF -> {
                    sId = id
                    sType = type
                    sChannel = -1
                }
                else -> {
                    return false
                }
            }
            return true
        }
    }

}
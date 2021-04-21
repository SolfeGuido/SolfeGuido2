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

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.IntIntMap
import io.github.solfeguido.factories.gdxIntIntMapOf
import java.io.InputStream
import java.io.OutputStream

open class ChannelEvent : MidiEvent{

    companion object {
        const val NOTE_OFF = 0x8
        const val NOTE_ON = 0x9
        const val NOTE_AFTERTOUCH = 0xA
        const val CONTROLLER = 0xB
        const val PROGRAM_CHANGE = 0xC
        const val CHANNEL_AFTERTOUCH = 0xD
        const val PITCH_BEND = 0xE


        fun parseChannelEvent(tick: Long, delta: Long, type: Int, channel: Int, input: InputStream): MidiEvent {
            val v1 = input.read()
            val v2 = when(type != PROGRAM_CHANGE && type != CHANNEL_AFTERTOUCH) {
                true -> input.read()
                else -> 0
            }

            return when(type) {
                NOTE_OFF -> NoteOff(tick, delta, channel, v1, v2)
                NOTE_ON -> NoteOn(tick, delta, channel, v1, v2)
                NOTE_AFTERTOUCH -> NoteAftertouch(tick, delta, channel, v1, v2)
                CONTROLLER -> Controller(tick, delta, channel, v1, v2)
                PROGRAM_CHANGE -> ProgramChange(tick, delta, channel, v1)
                CHANNEL_AFTERTOUCH -> ChannelAftertouch(tick, delta, channel, v1)
                PITCH_BEND -> PitchBend(tick, delta, channel, v1, v2)
                else -> ChannelEvent(tick, delta, type, channel, v1, v2)
            }
        }
    }

    var type: Int
        protected set
    var channel:Int
        private set
    var value1: Int
        protected  set
    var value2: Int
        protected  set
    val orderMap: IntIntMap

    constructor(tick: Long, type: Int, channel: Int, param1: Int, param2: Int) : this(tick, 0, type, channel, param1, param2)

    constructor(tick : Long, delta: Long, type: Int, channel: Int, param1: Int, param2: Int) : super(tick, delta)
    {
        this.type = type and 0x0F
        this.channel = channel and 0x0F
        this.value1 = param1 and 0xFF
        this.value2 = param2 and 0xFF
        orderMap = gdxIntIntMapOf(
                PROGRAM_CHANGE to 0,
                CONTROLLER to 1,
                NOTE_ON to 2,
                NOTE_OFF to 3,
                NOTE_AFTERTOUCH to 4,
                CHANNEL_AFTERTOUCH to 5,
                PITCH_BEND to 6
        )
    }

    override val eventSize: Int
        get() = when(type) {
            PROGRAM_CHANGE, CHANNEL_AFTERTOUCH -> 2
            else -> 3
        }

    fun setChannel(c : Int) {
        channel=  MathUtils.clamp(c, 0, 15)
    }

    override fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return 1
        if (tick != other.tick) {
            return if (tick < other.tick) -1 else 1
        }
        if (mDelta.varValue != other.mDelta.varValue) {
            return if (mDelta.varValue < other.mDelta.varValue) 1 else -1
        }

        if (other !is ChannelEvent) {
            return 1
        }

        if (type != other.type) {
            val order1: Int = orderMap[type, 0]
            val order2: Int = orderMap[other.type, 0]
            return if (order1 < order2) -1 else 1
        }
        if (value1 != other.value1) {
            return if (value1 < other.value1) -1 else 1
        }
        if (value2 != other.value2) {
            return if (value2 < other.value2) -1 else 1
        }
        return if (channel != other.channel) {
            if (channel < other.channel) -1 else 1
        } else 0
    }

    override fun requiresStatusByte(prevEvent: MidiEvent?): Boolean {
        if(prevEvent !is MidiEvent || prevEvent !is ChannelEvent) return true
        return type == prevEvent.type && channel == prevEvent.channel
    }

    override fun writeToFile(out: OutputStream, writeType: Boolean) {
        super.writeToFile(out, writeType)
        if(writeType) {
            val typeChannel = (type shl 4) + channel
            out.write(typeChannel)
        }

        out.write(value1)
        if(type != PROGRAM_CHANGE && type != CHANNEL_AFTERTOUCH) {
            out.write(value2)
        }
    }
}
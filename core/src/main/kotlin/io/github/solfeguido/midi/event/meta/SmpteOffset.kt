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

class SmpteOffset(tick: Long, delta: Long, var frameRate: FrameRate?, var hours: Int, var minutes: Int, var seconds: Int, var frames: Int, var subFrames: Int) :
        MetaEvent(tick, delta, SMPTE_OFFSET, VariableLengthInt(5)) {

    override val eventSize: Int
        protected get() = 8

    @Throws(IOException::class)
    public override fun writeToFile(out: OutputStream) {
        super.writeToFile(out)
        out.write(5)
        out.write(hours)
        out.write(minutes)
        out.write(seconds)
        out.write(frames)
        out.write(subFrames)
    }

    enum class FrameRate(val value: Int) {
        FRAME_RATE_24(0x00), FRAME_RATE_25(0x01), FRAME_RATE_30_DROP(0x02), FRAME_RATE_30(0x03);

        companion object {
            fun fromInt(`val`: Int): FrameRate? {
                when (`val`) {
                    0 -> return FRAME_RATE_24
                    1 -> return FRAME_RATE_25
                    2 -> return FRAME_RATE_30_DROP
                    3 -> return FRAME_RATE_30
                }
                return null
            }
        }

    }

    override fun compareTo(other: MidiEvent?): Int {
        if(other !is MidiEvent) return 1
        if (mTick != other.mTick) {
            return if (mTick < other.mTick) -1 else 1
        }
        if (mDelta.varValue.toLong() != other.delta) {
            return if (mDelta.varValue < other.delta) 1 else -1
        }
        return if (other !is SmpteOffset) {
            1
        } else 0
    }

    companion object {
        const val FRAME_RATE_24 = 0
        const val FRAME_RATE_25 = 1
        const val FRAME_RATE_30_DROP = 2
        const val FRAME_RATE_30 = 3
        fun parseSmpteOffset(tick: Long, delta: Long, info: MetaEventData): MetaEvent {
            if (info.length.varValue != 5) {
                return GenericMetaEvent(tick, delta, info)
            }
            val rrHours = info.data[0].toInt()
            val rr = rrHours shr 5
            val fps = FrameRate.fromInt(rr)
            val hour = rrHours and 0x1F
            val min = info.data[1].toInt()
            val sec = info.data[2].toInt()
            val frm = info.data[3].toInt()
            val sub = info.data[4].toInt()
            return SmpteOffset(tick, delta, fps, hour, min, sec, frm, sub)
        }
    }

}
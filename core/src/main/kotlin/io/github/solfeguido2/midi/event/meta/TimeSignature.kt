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

package io.github.solfeguido2.midi.event.meta


import io.github.solfeguido2.midi.event.MidiEvent
import io.github.solfeguido2.midi.util.VariableLengthInt
import java.io.OutputStream
import kotlin.math.pow


class TimeSignature @JvmOverloads constructor(
    tick: Long = 0,
    delta: Long = 0,
    num: Int = 4,
    den: Int = 4,
    meter: Int = DEFAULT_METER,
    div: Int = DEFAULT_DIVISION
) :
    MetaEvent(tick, delta, TIME_SIGNATURE, VariableLengthInt(4)) {
    var numerator = 0
        private set
    var denominatorValue = 0
        private set
    var meter = 0
        private set
    var division = 0
        private set

    fun setTimeSignature(num: Int, den: Int, meter: Int, div: Int) {
        numerator = num
        denominatorValue = log2(den)
        this.meter = meter
        division = div
    }

    val realDenominator: Int
        get() = Math.pow(2.0, denominatorValue.toDouble()).toInt()

    override val eventSize: Int
        get() = 7

    public override fun writeToFile(out: OutputStream) {
        super.writeToFile(out)
        out.write(4)
        out.write(numerator)
        out.write(denominatorValue)
        out.write(meter)
        out.write(division)
    }

    private fun log2(den: Int): Int {
        when (den) {
            2 -> return 1
            4 -> return 2
            8 -> return 3
            16 -> return 4
            32 -> return 5
        }
        return 0
    }

    override fun toString(): String {
        return super.toString() + " " + numerator + "/" + realDenominator
    }

    override fun compareTo(other: MidiEvent?): Int {
        if (other !is MidiEvent) return 1

        if (mTick != other.mTick) {
            return if (mTick < other.mTick) -1 else 1
        }
        if (mDelta.varValue.toLong() != other.delta) {
            return if (mDelta.varValue < other.delta) 1 else -1
        }
        if (other !is TimeSignature) {
            return 1
        }
        if (numerator != other.numerator) {
            return if (numerator < other.numerator) -1 else 1
        }
        return if (denominatorValue != other.denominatorValue) {
            if (denominatorValue < other.denominatorValue) -1 else 1
        } else 0
    }

    companion object {
        const val METER_EIGHTH = 12
        const val METER_QUARTER = 24
        const val METER_HALF = 48
        const val METER_WHOLE = 96
        const val DEFAULT_METER = METER_QUARTER
        const val DEFAULT_DIVISION = 8
        fun parseTimeSignature(tick: Long, delta: Long, info: MetaEventData): MetaEvent {
            if (info.length.varValue != 4) {
                return GenericMetaEvent(tick, delta, info)
            }
            val num = info.data[0].toInt()
            var den = info.data[1].toInt()
            val met = info.data[2].toInt()
            val fps = info.data[3].toInt()
            den = 2.0.pow(den.toDouble()).toInt()
            return TimeSignature(tick, delta, num, den, met, fps)
        }
    }

    init {
        setTimeSignature(num, den, meter, div)
    }
}
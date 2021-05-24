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


class PitchBend : ChannelEvent {
    constructor(tick: Long, channel: Int, lsb: Int, msb: Int) : super(tick, PITCH_BEND, channel, lsb, msb) {}
    constructor(tick: Long, delta: Long, channel: Int, lsb: Int, msb: Int) : super(tick, delta, PITCH_BEND, channel, lsb, msb) {}

    var leastSignificantBits: Int
        get() = value1
        set(p) {
            value1 = p and 0x7F
        }

    var mostSignificantBits: Int
        get() = value2
        set(p) {
            value2 = p and 0x7F
        }

    var bendAmount: Int
        get() {
            val y: Int = value2 and 0x7F shl 7
            val x: Int = value1
            return y + x
        }
        set(amount) {
            var am = amount and 0x3FFF
            value1 = am and 0x7F
            value2 = am shr 7
        }

}
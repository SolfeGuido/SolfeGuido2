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

package io.github.solfeguido.midi.util

import ktx.log.info
import kotlin.experimental.and

/**
 * MIDI Unit Conversions
 */
fun ticksToMs(ticks: Long, mpqn: Int, resolution: Int): Long {
    return ticks * mpqn / resolution / 1000
}

fun ticksToMs(ticks: Long, bpm: Float, resolution: Int): Long {
    return ticksToMs(ticks, bpmToMpqn(bpm), resolution)
}

fun msToTicks(ms: Long, mpqn: Int, ppq: Int): Double {
    return ms * 1000.0 * ppq / mpqn
}

fun msToTicks(ms: Long, bpm: Float, ppq: Int): Double {
    return msToTicks(ms, bpmToMpqn(bpm), ppq)
}

fun bpmToMpqn(bpm: Float): Int {
    return (bpm * 60000000).toInt()
}

fun mpqnToBpm(mpqn: Int): Float {
    return mpqn / 60000000.0f
}

/**
 * Utility methods for working with bytes and byte buffers
 */
fun bytesToInt(buff: ByteArray, off: Int, len: Int): Int {
    var num = 0
    var shift = 0
    for (i in off + len - 1 downTo off) {
        num += buff[i].toInt().and(0xFF).shl(shift)
        shift += 8
    }
    return num
}

fun intToBytes(value: Int, byteCount: Int): ByteArray {
    var tmpValue = value
    val buffer = ByteArray(byteCount)
    val ints = IntArray(byteCount)
    for (i in 0 until byteCount) {
        ints[i] = tmpValue and 0xFF
        buffer[byteCount - i - 1] = ints[i].toByte()
        tmpValue = tmpValue shr 8
        if (tmpValue == 0) {
            break
        }
    }
    return buffer
}

fun bytesEqual(buf1: ByteArray, buf2: ByteArray, off: Int, len: Int): Boolean {
    for (i in off until off + len) {
        if (i >= buf1.size || i >= buf2.size) {
            return false
        }
        if (buf1[i] != buf2[i]) {
            return false
        }
    }
    return true
}

fun extractBytes(buffer: ByteArray, off: Int, len: Int): ByteArray {
    val ret = ByteArray(len)
    for (i in 0 until len) {
        ret[i] = buffer[off + i]
    }
    return ret
}

private const val HEX = "0123456789ABCDEF"

fun byteToHex(b: Byte): String {
    val high: Int = b.and(0xF0.toByte()).toInt().shr(4)
    val low: Int = b.and(0x0F).toInt()
    return "" + HEX[high] + HEX[low]
}

fun bytesToHex(b: ByteArray): String {
    val sb = StringBuilder()
    for (i in b.indices) {
        sb.append(byteToHex(b[i])).append(" ")
    }
    return sb.toString()
}
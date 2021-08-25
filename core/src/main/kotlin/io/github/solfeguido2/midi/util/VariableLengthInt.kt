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

package io.github.solfeguido2.midi.util

import java.io.InputStream

class VariableLengthInt {

    var varValue: Int = 0
    var bytes = byteArrayOf()
    var sizeInBytes: Int = 0

    constructor(value: Int) {
        updateValue(value)
    }

    constructor(input: InputStream) {
        parseBytes(input)
    }

    fun updateValue(value: Int) {
        varValue = value
        buildBytes()
    }

    private fun parseBytes(input: InputStream) {
        val ints = IntArray(4)
        sizeInBytes = 0
        varValue = 0

        var shift = 0
        var b: Int = input.read()
        while (sizeInBytes < 4) {
            sizeInBytes++
            val variable = b and 0x80 > 0
            if (!variable) {
                ints[sizeInBytes - 1] = b and 0x7F
                break
            }
            ints[sizeInBytes - 1] = b and 0x7F
            b = input.read()
        }
        for (i in 1 until sizeInBytes) {
            shift += 7
        }
        bytes = ByteArray(sizeInBytes)
        for (i in 0 until sizeInBytes) {
            bytes[i] = ints[i].toByte()
            varValue += ints[i] shl shift
            shift -= 7
        }
    }

    private fun buildBytes(){
        if(varValue == 0) {
            bytes = byteArrayOf(0x00)
            sizeInBytes = 1
            return
        }

        sizeInBytes = 0
        val vals = intArrayOf(0,0,0,0)
        var tmpVal = varValue

        while (sizeInBytes < 4 && tmpVal > 0){
            vals[sizeInBytes] = tmpVal and 0x7F
            sizeInBytes++
            tmpVal = tmpVal shr 7
        }

        (1 until sizeInBytes).forEach { vals[it] = vals[it] or 0x80 }

        bytes = ByteArray(sizeInBytes)
        (0 until sizeInBytes).forEach { bytes[it] = vals[sizeInBytes - it - 1].toByte() }
    }


    override fun toString(): String {
        return "${bytesToHex(bytes)}($varValue)"
    }
}
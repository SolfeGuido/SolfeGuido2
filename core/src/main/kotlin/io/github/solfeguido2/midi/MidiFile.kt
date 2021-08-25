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

package io.github.solfeguido2.midi

import io.github.solfeguido2.midi.util.bytesEqual
import io.github.solfeguido2.midi.util.bytesToInt
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.collections.toGdxArray
import ktx.log.info
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream

class MidiFile(inputStream: InputStream) {

    val type: Int
    val trackCount: Int
    val resolution: Int

    val tracks: GdxArray<MidiTrack>

    val lengthInTicks
        get() = tracks.maxByOrNull { it.lengthInTicks }

    companion object{
        const val HEADER_SIZE = 14
        val IDENTIFIER = byteArrayOf(
                'M'.code.toByte(),
                'T'.code.toByte(),
                'h'.code.toByte(),
                'd'.code.toByte()
        )
        const val DEFAULT_RESOLUTION = 480
    }

    init {
        val inBuffer = BufferedInputStream(inputStream)
        val buffer = ByteArray(HEADER_SIZE)
        inBuffer.read(buffer)

        if(!bytesEqual(buffer, IDENTIFIER, 0, 4)) {
            ktx.log.error { "File identifier MThd not found, Exiting" }
            type = 0
            trackCount = 0
            resolution = DEFAULT_RESOLUTION
            tracks = gdxArrayOf()
        } else {
            type = bytesToInt(buffer,8 ,2)
            trackCount = bytesToInt(buffer, 10, 2)
            resolution = bytesToInt(buffer, 12 ,2)

            tracks = (0 until trackCount).map { MidiTrack(inBuffer) }.toGdxArray()
        }
    }


}
